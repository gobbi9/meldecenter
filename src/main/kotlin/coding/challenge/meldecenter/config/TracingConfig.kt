package coding.challenge.meldecenter.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.Tracer
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.WebFilter
import reactor.core.publisher.Hooks
import java.util.concurrent.ConcurrentHashMap

private val log = KotlinLogging.logger {}

@Configuration
class TracingConfig {

    private val traceIdMap = ConcurrentHashMap<String, String>()

    @PostConstruct
    fun init() {
        log.info { "Enabling automatic context propagation for Reactor" }
        Hooks.enableAutomaticContextPropagation()
    }

    /**
     * Stellt eine WebFilter-Bean bereit, um die Trace-ID und Span-ID des aktuellen Spans
     * zur Nachverfolgung in eine threadsichere Map zu übertragen. Verwendet den HTTP-Header
     * „X-Logbook-Key“, um Anfragen mit ihren entsprechenden Trace- und Span-IDs zu korrelieren.
     */
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

    fun getTraceIdMap(): ConcurrentHashMap<String, String> = traceIdMap
}
