package coding.challenge.meldecenter.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.Span
import io.micrometer.tracing.Tracer
import io.r2dbc.proxy.ProxyConnectionFactory
import io.r2dbc.proxy.core.QueryExecutionInfo
import io.r2dbc.proxy.listener.ProxyExecutionListener
import io.r2dbc.proxy.support.QueryExecutionInfoFormatter
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
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
class R2dbcProxyConfig(private val tracer: Tracer) {

    private val formatter = QueryExecutionInfoFormatter.showAll()

    @Bean
    @Primary
    fun connectionFactory(connectionDetails: R2dbcConnectionDetails): ConnectionFactory {
        val options = connectionDetails.connectionFactoryOptions
        val dbName =
            options.getValue(ConnectionFactoryOptions.DATABASE) as String?
        val dbUser = options.getValue(ConnectionFactoryOptions.USER) as String?
        val original = ConnectionFactories.get(options)

        return ProxyConnectionFactory.builder(original)
            .listener(object : ProxyExecutionListener {
                override fun beforeQuery(execInfo: QueryExecutionInfo) {
                    val span = tracer.spanBuilder()
                        .name("sql.query")
                        .kind(Span.Kind.CLIENT)
                        .tag("db.system", "postgresql")
                        .tag("db.name", dbName ?: "")
                        .tag("db.user", dbUser ?: "")

                    val queries = execInfo.queries
                    if (queries.isNotEmpty()) {
                        span.tag(
                            "db.statement",
                            queries.joinToString("; ") { it.query })
                    }

                    val startedSpan = span.start()
                    execInfo.valueStore.put(Span::class.java, startedSpan)
                }

                override fun afterQuery(execInfo: QueryExecutionInfo) {
                    val span =
                        execInfo.valueStore.get(
                            Span::class.java,
                            Span::class.java
                        )
                    try {
                        if (span != null) {
                            tracer.withSpan(span).use {
                                log.trace { "SQL: ${formatter.format(execInfo)}" }
                            }
                        } else {
                            log.trace { "SQL: ${formatter.format(execInfo)}" }
                        }

                        if (span != null && !execInfo.isSuccess) {
                            execInfo.throwable?.let { span.error(it) }
                        }
                    } finally {
                        span?.end()
                    }
                }
            })
            .build()
    }
}
