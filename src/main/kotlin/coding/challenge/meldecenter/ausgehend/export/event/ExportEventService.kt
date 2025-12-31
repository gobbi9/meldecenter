package coding.challenge.meldecenter.ausgehend.export.event

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportStatus
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
    suspend fun saveAssignedEvent(exportId: Long) {
        val exportEvent = newExportEvent(
            exportId = exportId,
            type = ExportEventType.ASSIGNED,
            traceId = tracer.currentTraceId()
        )
        saveExportEvent(exportEvent)
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
    suspend fun saveEndOrErrorEvent(export: ExportEntity) {
        val exportEventType = when (export.status) {
            ExportStatus.EXPORTED -> ExportEventType.END
            ExportStatus.FAILED -> ExportEventType.ERROR
            else -> throw IllegalArgumentException("Unbekannter Status: ${export.status}")
        }
        val exportEvent = newExportEvent(
            exportId = export.id,
            type = exportEventType,
            traceId = tracer.currentTraceId()
        )
        saveExportEvent(exportEvent)
    }
}
