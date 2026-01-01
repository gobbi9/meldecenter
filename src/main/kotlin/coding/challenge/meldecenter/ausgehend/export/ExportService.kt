package coding.challenge.meldecenter.ausgehend.export

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class ExportService(
    private val exportRepository: ExportRepository,
) {

    @NewSpan
    suspend fun insert(newExport: ExportEntity): ExportEntity {
        log.trace { "Speichere Export: $newExport" }
        val savedExport = exportRepository.save(newExport)
        log.trace { "Export gespeichert: $savedExport" }
        return savedExport
    }

    @NewSpan
    suspend fun nullAndDeleteUnassignedExport(exportId: Long): ExportEntity? {
        log.warn { "Race condition oder Export mit nur Duplikate? Export mit ID: $exportId wird gelöscht." }
        exportRepository.deleteById(exportId)
        log.debug { "Export mit ID: $exportId wurde gelöscht." }
        return null
    }

    @NewSpan
    suspend fun findById(exportId: Long): ExportEntity? {
        log.debug { "Export mit ID: $exportId wird gefunden." }
        return exportRepository.findById(exportId)
    }

    @NewSpan
    suspend fun updateStatus(exportId: Long, status: ExportStatus) {
        log.debug { "Aktualisiere Export Status: $exportId -> $status" }
        val updateCount = exportRepository.updateStatus(
            id = exportId,
            newStatus = status,
            currentStatus = ExportStatus.EXPORTING
        )
        if (updateCount > 0) {
            log.debug { "Aktualisiert Export Status: $exportId -> $status" }
            return
        }
        log.warn { "Race condition? Export ID: $exportId nicht gefunden" }
    }
}
