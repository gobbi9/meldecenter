package coding.challenge.meldecenter.config

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.context.ContextSnapshot
import io.micrometer.observation.Observation
import io.micrometer.observation.ObservationRegistry
import io.micrometer.tracing.Tracer
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.ThreadContextElement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactor.ReactorContext
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import reactor.util.context.Context
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private val log = KotlinLogging.logger {}

/**
 * Workaround, damit @NewSpan mit coroutines funktioniert.
 * Verwendet Micrometer Observation für bessere Context-Propagation zu Reactor.
 */
@Aspect
@Component
class CoroutinesNewSpanAspect(
    private val tracer: Tracer,
    private val observationRegistry: ObservationRegistry,
) : Ordered {

    override fun getOrder(): Int = Ordered.HIGHEST_PRECEDENCE + 100

    @Around("@annotation(io.micrometer.tracing.annotation.NewSpan)")
    fun aroundNewSpan(pjp: ProceedingJoinPoint): Any? {
        val signature = pjp.signature as MethodSignature
        val method = signature.method
        val newSpanAnnotation = method.getAnnotation(NewSpan::class.java)
        val spanName = newSpanAnnotation.name.ifEmpty {
            "${signature.declaringType.simpleName}.${method.name}"
        }

        val isSuspend =
            pjp.args.isNotEmpty() && pjp.args.last() is Continuation<*>

        if (isSuspend) {
            return handleSuspend(pjp, spanName)
        }

        val observation = Observation.start(spanName, observationRegistry)
        return try {
            observation.openScope().use {
                val result = pjp.proceed()
                if (result is Flow<*>) {
                    return handleFlow(result, observation)
                }
                observation.stop()
                result
            }
        } catch (e: Throwable) {
            observation.error(e)
            observation.stop()
            throw e
        }
    }

    private fun handleSuspend(
        pjp: ProceedingJoinPoint,
        spanName: String,
    ): Any? {
        val args = pjp.args
        val continuation = args.last() as Continuation<Any?>

        val observation = Observation.start(spanName, observationRegistry)

        val newArgs = args.copyOf()
        val wrappedContinuation = object : Continuation<Any?> {
            override val context: CoroutineContext
                get() = continuation.context +
                    ObservationContextElement(observation) +
                    createReactorContext(continuation.context, observation)

            override fun resumeWith(result: Result<Any?>) {
                val value = result.getOrNull()
                if (value is Flow<*>) {
                    continuation.resumeWith(
                        Result.success(
                            handleFlow(
                                value,
                                observation,
                                continuation.context
                            )
                        )
                    )
                    return
                }

                try {
                    result.exceptionOrNull()?.let { observation.error(it) }
                    continuation.resumeWith(result)
                } finally {
                    observation.stop()
                }
            }
        }
        newArgs[newArgs.size - 1] = wrappedContinuation

        return observation.openScope().use {
            try {
                val result = pjp.proceed(newArgs)
                if (result != kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED) {
                    if (result is Flow<*>) {
                        return handleFlow(
                            result,
                            observation,
                            continuation.context
                        )
                    }
                    observation.stop()
                }
                result
            } catch (e: Throwable) {
                observation.error(e)
                observation.stop()
                throw e
            }
        }
    }

    private fun createReactorContext(
        coroutineContext: CoroutineContext,
        observation: Observation,
    ): ReactorContext {
        val currentReactorContext =
            coroutineContext[ReactorContext]?.context ?: Context.empty()
        return observation.openScope().use {
            val snapshot = ContextSnapshot.captureAll()
            ReactorContext(snapshot.updateContext(currentReactorContext))
        }
    }

    private fun handleFlow(
        flow: Flow<*>,
        observation: Observation,
        baseCoroutineContext: CoroutineContext? = null,
    ): Flow<*> {
        val reactorContext =
            createReactorContext(
                baseCoroutineContext ?: EmptyCoroutineContext,
                observation
            )
        return flow
            .onCompletion { observation.stop() }
            .flowOn(
                ObservationContextElement(observation) +
                    reactorContext
            )
    }

    class ObservationContextElement(
        val observation: Observation,
    ) : ThreadContextElement<Observation.Scope> {
        override val key: CoroutineContext.Key<*> get() = Key

        companion object Key : CoroutineContext.Key<ObservationContextElement>

        override fun updateThreadContext(context: CoroutineContext): Observation.Scope {
            return observation.openScope()
        }

        override fun restoreThreadContext(
            context: CoroutineContext,
            oldState: Observation.Scope,
        ) {
            oldState.close()
        }
    }
}
