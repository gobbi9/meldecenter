package coding.challenge.meldecenter.ausgehend.export.event

import coding.challenge.meldecenter.config.currentTraceId
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.Tracer
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 * Service zur Protokollierung von Export-Events.
 *
 * Ermöglicht das Speichern von Start-, Ende- und Fehlerereignissen für Exporte.
 */
@Service
class ExportEventService(
    private val exportEventRepository: ExportEventRepository,
    private val tracer: Tracer,
) {
    /**
     * Speichert ein [ExportEventEntity] in der Datenbank.
     *
     * @param exportEvent Das zu speichernde Event.
     */
    @NewSpan
    suspend fun saveExportEvent(exportEvent: ExportEventEntity) {
        log.trace { "Speichere ExportEvent: $exportEvent" }
        val savedExportEvent = exportEventRepository.save(exportEvent)
        log.trace { "ExportEvent gespeichert: $savedExportEvent" }
    }

    /**
     * Erstellt und speichert ein Start-Event für einen Export.
     *
     * @param exportId Die ID des Exports.
     */
    @NewSpan
    suspend fun saveStartEvent(exportId: Long) {
        val exportEvent = newExportEvent(
            exportId = exportId,
            type = ExportEventType.START,
            traceId = tracer.currentTraceId()
        )
        saveExportEvent(exportEvent)
    }

    /**
     * Erstellt und speichert ein Ende-Event (Erfolg) für einen Export.
     *
     * @param exportId Die ID des Exports.
     */
    @NewSpan
    suspend fun saveEndEvent(exportId: Long) {
        val exportEvent = newExportEvent(
            exportId = exportId,
            type = ExportEventType.END,
            traceId = tracer.currentTraceId()
        )
        saveExportEvent(exportEvent)
    }

    /**
     * Erstellt und speichert ein Fehler-Event für einen Export.
     *
     * @param exportId Die ID des Exports.
     * @param error Die aufgetretene Ausnahme.
     */
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
