package coding.challenge.meldecenter.ausgehend.krankenkasse.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltbescheinigungAuRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

/**
 * Gruppiert Entgeltbescheinigungen Arbeitsunfähigkeit und weist sie
 * einer [ExportEntity] zu.
 */
@Service
class EntgeltbescheinigungenAuExportAssigner(
    private val entgeltbescheinigungAuRepository: EntgeltbescheinigungAuRepository,
) {
    @NewSpan
    fun findAllExportGroups(): Flow<EntgeltbescheinigungAuExportGroup> =
        entgeltbescheinigungAuRepository.findAllExportGroups()

    /**
     * Duplizierte Entgeltbescheinigungen werden ignoriert,
     * und dem "Duplicates Export" (ID = 1) zugewiesen.
     *
     * Weist Entgeltbescheinigungen einem Export zu.
     */
    @NewSpan
    @Transactional
    suspend fun deduplicateAndAssignToExport(
        betriebsnummer: String,
        exportId: Long
    ): Int {
        val duplicateCount = entgeltbescheinigungAuRepository.deduplicate()
        log.debug { "Duplikate Anzahl dem Duplicates Export zugewiesen: $duplicateCount" }
        return entgeltbescheinigungAuRepository.assignToExport(betriebsnummer, exportId)
    }
}
