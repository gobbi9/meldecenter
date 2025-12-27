package coding.challenge.meldecenter

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.HttpRequest
import io.micrometer.tracing.Tracer
import org.slf4j.MDC
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer
import org.springframework.context.annotation.Bean
import org.zalando.logbook.CorrelationId
import org.zalando.logbook.Precorrelation
import org.zalando.logbook.Correlation
import org.zalando.logbook.HttpLogWriter
import org.zalando.logbook.Logbook
import org.zalando.logbook.core.DefaultHttpLogWriter
import org.zalando.logbook.netty.LogbookServerHandler
import reactor.core.publisher.Hooks
import org.springframework.web.server.WebFilter
import java.util.concurrent.ConcurrentHashMap

private val log = KotlinLogging.logger {}

@SpringBootApplication
class MeldecenterApplication {
    private val traceIdMap = ConcurrentHashMap<String, String>()

    @PostConstruct
    fun init() {
        log.info { "Enabling automatic context propagation for Reactor" }
        Hooks.enableAutomaticContextPropagation()
    }

    @Bean
    fun traceIdBridgeFilter(tracer: Tracer): WebFilter {
        return WebFilter { exchange, chain ->
            val key = exchange.request.headers.getFirst("X-Logbook-Key")
            if (key != null) {
                val traceId = tracer.currentSpan()?.context()?.traceId()
                val spanId = tracer.currentSpan()?.context()?.spanId()
                if (traceId != null) {
                    traceIdMap[key] = "$traceId|$spanId"
                }
            }
            chain.filter(exchange)
        }
    }

    @Bean
    fun logbookNettyCustomizer(logbook: Logbook): NettyServerCustomizer {
        return NettyServerCustomizer { httpServer ->
            httpServer.doOnConnection { connection ->
                connection.addHandlerFirst("traceIdEnsurer", object : ChannelInboundHandlerAdapter() {
                    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                        if (msg is HttpRequest) {
                            val headers = msg.headers()
                            if (!headers.contains("X-Logbook-Key")) {
                                headers.add("X-Logbook-Key", java.util.UUID.randomUUID().toString())
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
            request.headers["X-Logbook-Key"]?.firstOrNull() ?: java.util.UUID.randomUUID().toString()
        }
    }

    @Bean
    fun mdcHttpLogWriter(): HttpLogWriter {
        val delegate = DefaultHttpLogWriter()
        return object : HttpLogWriter {
            override fun write(precorrelation: Precorrelation, request: String) {
                writeWithMdc(precorrelation.id) { delegate.write(precorrelation, request) }
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

fun main(args: Array<String>) {
    runApplication<MeldecenterApplication>(*args)
}

// Kotlin map type DEUEV_ANMELDUNG to a generic content, maybe delegation or generics?
// JUST INTERFACE WITH "request" as property, AND REDIRECT TO CORRECT SERVICE
// or SEALED CLASS that wraps all these properties and implements an interface with request object
/*

For the DEÜV-Anmeldung.json a good deduplication strategy would be to use a combination of
- request.type (probably not needed, since each type has its own table)
- mitarbeiter_id
- beschaeftigung_beginn
then the message with the newest request.createdAt timestamp should be the winner.
Use PostgreSQL PARTITION BY

For Entgeltbescheinigung-Arbeitsunfähigkeit.json
- mitarbeiter_id
- krankheit_arbeitsunfaehigkeit_beginn
- krankheit_arbeitsunfaehigkeit_ende
 */
