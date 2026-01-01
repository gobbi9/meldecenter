package coding.challenge.meldecenter.ausgehend.sozialversicherung.export

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

/**
 * Service zum Gruppieren von DEÜV-Anmeldungen und zum Zuweisen zu einem Export.
 *
 * @property repository Das Repository für DEÜV-Anmeldungen.
 */
@Service
class DeuevAnmeldungExportAssigner(
    private val repository: DeuevAnmeldungExportRepository,
) {
    /**
     * Identifiziert Duplikate und weist die verbleibenden DEÜV-Anmeldungen einem Export zu.
     *
     * Duplizierte DEÜV-Anmeldungen werden ignoriert und dem "Duplicates Export" (ID = 1) zugewiesen.
     * Alle anderen offenen DEÜV-Anmeldungen der Betriebsnummer werden der angegebenen [exportId] zugeordnet.
     *
     * @param betriebsnummer Die Betriebsnummer des Arbeitgebers.
     * @param exportId Die ID des Exports, dem die Meldungen zugewiesen werden sollen.
     * @return Die Anzahl der erfolgreich zugewiesenen Meldungen.
     */
    @NewSpan
    @Transactional
    suspend fun deduplicateAndAssignToExport(
        betriebsnummer: String,
        exportId: Long,
    ): Int {
        val duplicateCount = repository.deduplicate(betriebsnummer)
        log.debug { "Duplikate Anzahl dem Duplicates Export zugewiesen: $duplicateCount" }
        return repository.assignToExport(betriebsnummer, exportId)
    }
}
