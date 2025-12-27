package coding.challenge.meldecenter.config

import io.micrometer.tracing.Tracer
import io.micrometer.tracing.annotation.NewSpan
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import kotlin.coroutines.CoroutineContext
import io.micrometer.context.ContextSnapshot
import kotlin.coroutines.Continuation
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.ThreadContextElement
import org.slf4j.MDC

private val log = KotlinLogging.logger {}

@Aspect
@Component
class CoroutinesNewSpanAspect(private val tracer: Tracer) {

    @Around("@annotation(io.micrometer.tracing.annotation.NewSpan)")
    fun aroundNewSpan(pjp: ProceedingJoinPoint): Any? {
        val signature = pjp.signature as MethodSignature
        val method = signature.method
        val newSpanAnnotation = method.getAnnotation(NewSpan::class.java)
        val spanName = newSpanAnnotation.name.ifEmpty { method.name }

        val isSuspend = pjp.args.isNotEmpty() && pjp.args.last() is Continuation<*>

        if (isSuspend) {
            return handleSuspend(pjp, spanName)
        }

        val nextSpan = tracer.nextSpan().name(spanName)
        return tracer.withSpan(nextSpan.start()).use {
            try {
                pjp.proceed()
            } catch (e: Throwable) {
                nextSpan.error(e)
                throw e
            } finally {
                nextSpan.end()
            }
        }
    }

    private fun handleSuspend(pjp: ProceedingJoinPoint, spanName: String): Any? {
        val args = pjp.args
        val nextSpan = tracer.nextSpan().name(spanName).start()

        val newArgs = args.copyOf()
        val continuation = args.last() as Continuation<Any?>

        val wrappedContinuation = object : Continuation<Any?> {
            override val context: CoroutineContext
                get() = continuation.context + MdcContextElement(nextSpan.context().traceId(), nextSpan.context().spanId())

            override fun resumeWith(result: Result<Any?>) {
                try {
                    result.exceptionOrNull()?.let { nextSpan.error(it) }
                    continuation.resumeWith(result)
                } finally {
                    nextSpan.end()
                }
            }
        }
        newArgs[newArgs.size - 1] = wrappedContinuation

        val scope = tracer.withSpan(nextSpan)
        return try {
            val oldTraceId = MDC.get("traceId")
            val oldSpanId = MDC.get("spanId")
            MDC.put("traceId", nextSpan.context().traceId())
            MDC.put("spanId", nextSpan.context().spanId())
            try {
                pjp.proceed(newArgs)
            } finally {
                if (oldTraceId != null) MDC.put("traceId", oldTraceId) else MDC.remove("traceId")
                if (oldSpanId != null) MDC.put("spanId", oldSpanId) else MDC.remove("spanId")
            }
        } catch (e: Throwable) {
            nextSpan.error(e)
            nextSpan.end()
            throw e
        } finally {
            scope.close()
        }
    }

    class MdcContextElement(private val traceId: String, private val spanId: String) : ThreadContextElement<Map<String, String>> {
        override val key: CoroutineContext.Key<*> get() = Key
        companion object Key : CoroutineContext.Key<MdcContextElement>

        override fun updateThreadContext(context: CoroutineContext): Map<String, String> {
            val oldState = MDC.getCopyOfContextMap() ?: emptyMap()
            MDC.put("traceId", traceId)
            MDC.put("spanId", spanId)
            return oldState
        }

        override fun restoreThreadContext(context: CoroutineContext, oldState: Map<String, String>) {
            MDC.setContextMap(oldState)
        }
    }
}
