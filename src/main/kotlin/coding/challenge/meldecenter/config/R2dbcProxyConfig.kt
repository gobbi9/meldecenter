package coding.challenge.meldecenter.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationRegistry
import io.r2dbc.proxy.ProxyConnectionFactory
import io.r2dbc.proxy.core.QueryExecutionInfo
import io.r2dbc.proxy.listener.ProxyMethodExecutionListener
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty
import org.springframework.boot.autoconfigure.r2dbc.R2dbcConnectionDetails
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

private val log = KotlinLogging.logger {}

/**
 * Aktiviert Logging von R2DBC-Abfragen mit OpenTelemetry Spans.
 */
@Configuration
@ConditionalOnBooleanProperty(prefix = "logging", name = ["r2dbc"])
class R2dbcProxyConfig(
    private val observationRegistry: ObservationRegistry
) {

    private val queryObservationKey: String = "queryObservation"
    private val formatter = QueryExecutionInfoFormatter.showAll()

    @Bean
    @Primary
    fun connectionFactory(connectionDetails: R2dbcConnectionDetails): ConnectionFactory {
        val options = connectionDetails.connectionFactoryOptions
        val original = ConnectionFactories.get(options)

        return ProxyConnectionFactory.builder(original)
            .listener(object : ProxyMethodExecutionListener {
                override fun beforeQuery(execInfo: QueryExecutionInfo) {
                    val observation = Observation.start("sql.query", observationRegistry)

                    val queries = execInfo.queries
                    if (queries.isNotEmpty()) {
                        observation.highCardinalityKeyValue(
                            "db.statement",
                            queries.joinToString("; ") { it.query })
                    }

                    execInfo.valueStore.put(queryObservationKey, observation)
                }

                override fun afterQuery(execInfo: QueryExecutionInfo) {
                    val observation = execInfo.valueStore[queryObservationKey] as Observation?
                    try {
                        if (observation != null) {
                            observation.openScope().use {
                                log.trace { "SQL: ${formatter.format(execInfo)}" }
                            }
                        } else {
                            log.trace { "SQL: ${formatter.format(execInfo)}" }
                        }

                        if (observation != null && !execInfo.isSuccess) {
                            execInfo.throwable?.let { observation.error(it) }
                        }
                    } finally {
                        observation?.stop()
                    }
                }
            })
            .build()
    }
}
