package coding.challenge.meldecenter.ausgehend.sozialversicherung.export

import coding.challenge.meldecenter.shared.sozialversicherung.DeuevAnmeldungEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repository für den Zugriff auf [DeuevAnmeldungEntity]-Daten für Export-Zwecke.
 */
@Repository
interface DeuevAnmeldungExportRepository : CoroutineCrudRepository<DeuevAnmeldungEntity, UUID> {

    /**
     * Findet alle Exportgruppen (Betriebsnummer und Anzahl), die noch keiner Export-ID zugewiesen sind.
     *
     * @return Ein [Flow] von [DeuevAnmeldungExportGroup].
     */
    @Query(
        """
        SELECT ARBEITGEBER_BETRIEBSNUMMER AS BETRIEBSNUMMER, COUNT(*) AS COUNT
        FROM DEUEV_ANMELDUNG
        WHERE EXPORT_ID IS NULL
        GROUP BY ARBEITGEBER_BETRIEBSNUMMER
        """
    )
    fun findAllExportGroups(): Flow<DeuevAnmeldungExportGroup>

    /**
     * Identifiziert Duplikate für eine Betriebsnummer und weist sie dem Duplikate-Export zu.
     *
     * Duplizierte DEÜV-Anmeldungen werden ignoriert und der [duplicatesExportId] zugewiesen.
     *
     * @param betriebsnummer Die Betriebsnummer des Arbeitgebers.
     * @param duplicatesExportId Die ID des Exports für Duplikate (Standard: 1).
     * @return Die Anzahl der aktualisierten Zeilen.
     */
    @Query(
        """
        UPDATE DEUEV_ANMELDUNG D1
        SET EXPORT_ID = :duplicatesExportId
        WHERE ARBEITGEBER_BETRIEBSNUMMER = :betriebsnummer
            AND EXPORT_ID IS NULL
            AND EXISTS (
                SELECT 1 FROM DEUEV_ANMELDUNG D2
                WHERE D2.MELDUNG_ID = D1.MELDUNG_ID AND (
                    D2.EXPORT_ID IS NOT NULL
                    OR D2.AUDIT_CREATED_AT > D1.AUDIT_CREATED_AT
                )
          )
    """
    )
    @Modifying
    suspend fun deduplicate(betriebsnummer: String, duplicatesExportId: Long = 1): Int

    /**
     * Weist alle noch nicht exportierten DEÜV-Anmeldungen einer Betriebsnummer einer [exportId] zu.
     *
     * @param betriebsnummer Die Betriebsnummer des Arbeitgebers.
     * @param exportId Die ID des Ziel-Exports.
     * @return Die Anzahl der zugewiesenen Meldungen.
     */
    @Query(
        """
        UPDATE DEUEV_ANMELDUNG
        SET EXPORT_ID = :exportId
        WHERE ARBEITGEBER_BETRIEBSNUMMER = :betriebsnummer
        AND EXPORT_ID IS NULL
    """
    )
    @Modifying
    suspend fun assignToExport(betriebsnummer: String, exportId: Long): Int

    /**
     * Findet alle DEÜV-Anmeldungen, die einer bestimmten Export-ID zugewiesen sind.
     *
     * @param exportId Die ID des Exports.
     * @return Ein [Flow] von [DeuevAnmeldungEntity].
     */
    fun findByExportId(exportId: Long): Flow<DeuevAnmeldungEntity>
}
