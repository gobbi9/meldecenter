package coding.challenge.meldecenter.ausgehend.krankenkasse.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportService
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
    private val exportAssigner: EntgeltbescheinigungenAuExportAssigner,
    private val exportService: ExportService,
) {
    @NewSpan
    suspend fun assign(
        exportGroup: EntgeltbescheinigungAuExportGroup,
    ): ExportEntity? {
        log.trace { "Entgeltbescheinigungen Gruppe: $exportGroup" }
        val savedExport = createEmptyExport(exportGroup.betriebsnummer)
        val updatedCount = exportAssigner.deduplicateAndAssignToExport(
            exportGroup.betriebsnummer,
            exportId = savedExport.id
        )
        return when (updatedCount) {
            0 -> exportService.nullAndDeleteUnassignedExport(exportId = savedExport.id)
            else -> savedExport
        }
    }

    private suspend fun createEmptyExport(
        betriebsnummer: String,
    ): ExportEntity = exportService.insert(
        newExport(
            typ = MeldungTyp.ENTGELTBESCHEINIGUNG_KG,
            betriebsnummer = betriebsnummer,
        )
    )
}
