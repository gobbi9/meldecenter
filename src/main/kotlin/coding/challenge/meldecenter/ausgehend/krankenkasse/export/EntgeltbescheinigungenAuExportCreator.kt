package coding.challenge.meldecenter.ausgehend.krankenkasse.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportRepository
import coding.challenge.meldecenter.ausgehend.export.newExport
import coding.challenge.meldecenter.eingehend.shared.MeldungTyp
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 *  Erstellt ein [ExportEntity], und ggf. löscht nicht zugeordnete Exports.
 */
@Service
class EntgeltbescheinigungenAuExportCreator(
    private val entgeltbescheinigungenAuExportAssigner: EntgeltbescheinigungenAuExportAssigner,
    private val exportRepository: ExportRepository,
) {
    @NewSpan
    suspend fun assign(
        exportGroup: EntgeltbescheinigungAuExportGroup,
    ): ExportEntity? {
        log.trace { "Entgeltbescheinigungen Gruppe: $exportGroup" }
        val savedExport = createEmptyExport(exportGroup.betriebsnummer)
        val updatedCount =
            entgeltbescheinigungenAuExportAssigner.deduplicateAndAssignToExport(
                exportGroup.betriebsnummer,
                exportId = savedExport.id
            )
        return when (updatedCount) {
            0 -> nullAndDeleteUnassignedExport(exportId = savedExport.id)
            else -> savedExport
        }
    }

    private suspend fun createEmptyExport(
        betriebsnummer: String,
    ): ExportEntity {
        val export = newExport(
            typ = MeldungTyp.ENTGELTBESCHEINIGUNG_KG,
            betriebsnummer = betriebsnummer,
        )
        log.trace { "Speichere Export: $export" }
        val savedExport = exportRepository.save(export)
        log.trace { "Export gespeichert: $savedExport" }
        return savedExport
    }

    private suspend fun nullAndDeleteUnassignedExport(
        exportId: Long,
    ): ExportEntity? {
        log.warn { "Race condition? Export mit ID: $exportId wird gelöscht." }
        exportRepository.deleteById(exportId)
        log.debug { "Export mit ID: $exportId wurde gelöscht." }
        return null
    }
}
