package coding.challenge.meldecenter.ausgehend.export

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 * Service für die Verwaltung von Exportvorgängen.
 *
 * Stellt Methoden zum Speichern, Löschen und Aktualisieren von Exporten bereit.
 */
@Service
class ExportService(
    private val exportRepository: ExportRepository,
) {

    /**
     * Speichert eine neue Export-Entität.
     *
     * @param newExport Die zu speichernde [ExportEntity].
     * @return Die gespeicherte Entität mit generierter ID.
     */
    @NewSpan
    suspend fun insert(newExport: ExportEntity): ExportEntity {
        log.trace { "Speichere Export: $newExport" }
        val savedExport = exportRepository.save(newExport)
        log.trace { "Export gespeichert: $savedExport" }
        return savedExport
    }

    /**
     * Löscht einen Export, dem keine Daten zugeordnet wurden (z.B. bei Duplikaten).
     *
     * @param exportId Die ID des zu löschenden Exports.
     * @return Immer `null`.
     */
    @NewSpan
    suspend fun nullAndDeleteUnassignedExport(exportId: Long): ExportEntity? {
        log.warn { "Race condition oder Export mit nur Duplikate? Export mit ID: $exportId wird gelöscht." }
        exportRepository.deleteById(exportId)
        log.debug { "Export mit ID: $exportId wurde gelöscht." }
        return null
    }

    /**
     * Sucht einen Export anhand seiner ID.
     *
     * @param exportId Die ID des Exports.
     * @return Die gefundene [ExportEntity] oder `null`.
     */
    @NewSpan
    suspend fun findById(exportId: Long): ExportEntity? {
        log.debug { "Export mit ID: $exportId wird gefunden." }
        return exportRepository.findById(exportId)
    }

    /**
     * Aktualisiert den Status eines laufenden Exports.
     *
     * Erwartet, dass der aktuelle Status [ExportStatus.EXPORTING] ist.
     *
     * @param exportId Die ID des Exports.
     * @param status Der neue Status.
     */
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
