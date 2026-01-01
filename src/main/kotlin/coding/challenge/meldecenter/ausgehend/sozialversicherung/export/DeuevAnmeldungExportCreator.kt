package coding.challenge.meldecenter.ausgehend.sozialversicherung.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportService
import coding.challenge.meldecenter.ausgehend.export.newExport
import coding.challenge.meldecenter.eingehend.shared.MeldungTyp
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 * Service zur Erstellung von Export-Entitäten für DEÜV-Anmeldungen.
 *
 * Erstellt eine neue [ExportEntity] und löscht diese wieder, falls keine Meldungen zugeordnet werden konnten.
 *
 * @property exportAssigner Der Assigner zur Zuweisung von Meldungen zum Export.
 * @property exportService Der Service zur Verwaltung von Exporten.
 */
@Service
class DeuevAnmeldungExportCreator(
    private val exportAssigner: DeuevAnmeldungExportAssigner,
    private val exportService: ExportService,
) {
    /**
     * Erstellt einen Export für die angegebene Gruppe und ordnet die Meldungen zu.
     *
     * Falls nach der Bereinigung von Duplikaten keine Meldungen übrig bleiben, wird der erstellte
     * Export wieder gelöscht.
     *
     * @param exportGroup Die Gruppe von DEÜV-Anmeldungen (basierend auf Betriebsnummer).
     * @return Die erstellte [ExportEntity] oder `null`, falls keine Meldungen zugeordnet wurden.
     */
    @NewSpan
    suspend fun assign(
        exportGroup: DeuevAnmeldungExportGroup,
    ): ExportEntity? {
        log.trace { "DEÜV-Anmeldungen Gruppe: $exportGroup" }
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
            typ = MeldungTyp.DEUEV_ANMELDUNG,
            betriebsnummer = betriebsnummer,
        )
    )
}
