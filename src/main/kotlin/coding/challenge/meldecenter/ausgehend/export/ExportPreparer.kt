package coding.challenge.meldecenter.ausgehend.export

import coding.challenge.meldecenter.ausgehend.export.event.ExportEventService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 *  Bereitet Exports vor, bevor sie zu dem FTP Server hochgeladen werden.
 */
@Service
class ExportPreparer(
    private val exportRepository: ExportRepository,
    private val exportEventService: ExportEventService,
) {
    @NewSpan
    suspend fun prepare(export: ExportEntity): ExportEntity? {
        log.debug { "Vorbereiten zum Hochladen für Export ID: ${export.id}" }
        val updateCount = exportRepository.updateStatus(
            id = export.id,
            newStatus = ExportStatus.EXPORTING,
            currentStatus = ExportStatus.CREATED,
        )
        return when (updateCount) {
            0 -> nullAndLogIt(export)
            1 -> findUpdatedExport(exportId = export.id)
            else -> error("Mehr als ein Export mit ID: ${export.id} aktualisiert.")
        }
    }

    private suspend fun nullAndLogIt(export: ExportEntity): ExportEntity? {
        log.warn { "Race condition? Export mit ID: ${export.id} hat status: ${export.status}." }
        return null
    }

    private suspend fun findUpdatedExport(exportId: Long): ExportEntity? {
        val newStatus = ExportStatus.EXPORTING
        val updatedExport = exportRepository.findByIdAndStatus(
            id = exportId,
            status = newStatus
        )
        if (updatedExport == null) {
            log.error { "Export mit ID: $exportId und status: $newStatus nicht gefunden." }
        }
        exportEventService.saveStartEvent(exportId)
        return updatedExport
    }
}
