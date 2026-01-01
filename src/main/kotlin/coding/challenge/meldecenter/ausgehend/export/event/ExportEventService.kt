package coding.challenge.meldecenter.ausgehend.export.event

import coding.challenge.meldecenter.config.currentTraceId
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.Tracer
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class ExportEventService(
    private val exportEventRepository: ExportEventRepository,
    private val tracer: Tracer,
) {
    @NewSpan
    suspend fun saveExportEvent(exportEvent: ExportEventEntity) {
        log.trace { "Speichere ExportEvent: $exportEvent" }
        val savedExportEvent = exportEventRepository.save(exportEvent)
        log.trace { "ExportEvent gespeichert: $savedExportEvent" }
    }

    @NewSpan
    suspend fun saveStartEvent(exportId: Long) {
        val exportEvent = newExportEvent(
            exportId = exportId,
            type = ExportEventType.START,
            traceId = tracer.currentTraceId()
        )
        saveExportEvent(exportEvent)
    }

    @NewSpan
    suspend fun saveEndEvent(exportId: Long) {
        val exportEvent = newExportEvent(
            exportId = exportId,
            type = ExportEventType.END,
            traceId = tracer.currentTraceId()
        )
        saveExportEvent(exportEvent)
    }

    @NewSpan
    suspend fun saveErrorEvent(exportId: Long, error: Throwable) {
        val exportEvent = newExportEvent(
            exportId = exportId,
            type = ExportEventType.ERROR,
            details = error.message?.take(1000),
            traceId = tracer.currentTraceId()
        )
        saveExportEvent(exportEvent)
    }
}
