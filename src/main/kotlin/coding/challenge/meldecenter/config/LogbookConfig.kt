package coding.challenge.meldecenter.config

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.HttpRequest
import org.slf4j.MDC
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zalando.logbook.Correlation
import org.zalando.logbook.CorrelationId
import org.zalando.logbook.HttpLogWriter
import org.zalando.logbook.Logbook
import org.zalando.logbook.Precorrelation
import org.zalando.logbook.core.DefaultHttpLogWriter
import org.zalando.logbook.netty.LogbookServerHandler

/**
 * Workaround, damit traceId und spanId bei logbook Logs gesetzt werden,
 * und Unterstützung für Kotlin Coroutines
 */
@Configuration
class LogbookConfig(private val tracingConfig: TracingConfig) {

    @Bean
    fun logbookNettyCustomizer(logbook: Logbook): NettyServerCustomizer {
        return NettyServerCustomizer { httpServer ->
            httpServer.doOnConnection { connection ->
                connection.addHandlerFirst(
                    "traceIdEnsurer",
                    object : ChannelInboundHandlerAdapter() {
                        override fun channelRead(
                            ctx: ChannelHandlerContext,
                            msg: Any,
                        ) {
                            if (msg is HttpRequest) {
                                val headers = msg.headers()
                                if (!headers.contains("X-Logbook-Key")) {
                                    headers.add(
                                        "X-Logbook-Key",
                                        java.util.UUID.randomUUID().toString()
                                    )
                                }
                            }
                            super.channelRead(ctx, msg)
                        }
                    })
                connection.addHandlerLast(LogbookServerHandler(logbook))
            }
        }
    }

    @Bean
    fun traceIdCorrelationId(): CorrelationId {
        return CorrelationId { request ->
            request.headers["X-Logbook-Key"]?.firstOrNull() ?: java.util.UUID
                .randomUUID()
                .toString()
        }
    }

    @Bean
    fun mdcHttpLogWriter(): HttpLogWriter {
        val delegate = DefaultHttpLogWriter()
        val traceIdMap = tracingConfig.getTraceIdMap()
        return object : HttpLogWriter {
            override fun write(
                precorrelation: Precorrelation,
                request: String,
            ) {
                writeWithMdc(precorrelation.id) {
                    delegate.write(
                        precorrelation,
                        request
                    )
                }
            }

            override fun write(correlation: Correlation, response: String) {
                writeWithMdc(correlation.id) {
                    try {
                        delegate.write(correlation, response)
                    } finally {
                        traceIdMap.remove(correlation.id)
                    }
                }
            }

            private fun writeWithMdc(id: String, action: () -> Unit) {
                val springTrace = traceIdMap[id]
                val parts = springTrace?.split("|") ?: emptyList()
                val traceId = parts.getOrNull(0) ?: ""
                val spanId = parts.getOrNull(1) ?: ""

                if (traceId.isNotEmpty()) MDC.put("traceId", traceId)
                if (spanId.isNotEmpty()) MDC.put("spanId", spanId)
                try {
                    action()
                } finally {
                    MDC.remove("traceId")
                    MDC.remove("spanId")
                }
            }
        }
    }
}
