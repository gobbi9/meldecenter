package coding.challenge.meldecenter.ausgehend.krankenkasse.export

import coding.challenge.meldecenter.shared.krankenkasse.EntgeltbescheinigungAuEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repository für den Zugriff auf [EntgeltbescheinigungAuEntity]-Daten für Export-Zwecke.
 */
@Repository
interface EntgeltbescheinigungAuExportRepository : CoroutineCrudRepository<EntgeltbescheinigungAuEntity, UUID> {

    /**
     * Findet alle Exportgruppen (Betriebsnummer und Anzahl), die noch keiner Export-ID zugewiesen sind.
     *
     * @return Ein [Flow] von [EntgeltbescheinigungAuExportGroup].
     */
    @Query(
        """
        SELECT ARBEITGEBER_BETRIEBSNUMMER AS BETRIEBSNUMMER, COUNT(*) AS COUNT
        FROM ENTGELTBESCHEINIGUNG_AU
        WHERE EXPORT_ID IS NULL
        GROUP BY ARBEITGEBER_BETRIEBSNUMMER
        """
    )
    fun findAllExportGroups(): Flow<EntgeltbescheinigungAuExportGroup>

    /**
     * Identifiziert Duplikate für eine Betriebsnummer und weist sie dem Duplikate-Export zu.
     *
     * Duplizierte Entgeltbescheinigungen werden ignoriert und der [duplicatesExportId] zugewiesen.
     *
     * @param betriebsnummer Die Betriebsnummer des Arbeitgebers.
     * @param duplicatesExportId Die ID des Exports für Duplikate (Standard: 1).
     * @return Die Anzahl der aktualisierten Zeilen.
     */
    @Query(
        """
        UPDATE ENTGELTBESCHEINIGUNG_AU E1
        SET EXPORT_ID = :duplicatesExportId
        WHERE ARBEITGEBER_BETRIEBSNUMMER = :betriebsnummer
            AND EXPORT_ID IS NULL
            AND EXISTS (
                SELECT 1 FROM ENTGELTBESCHEINIGUNG_AU E2
                WHERE E2.MELDUNG_ID = E1.MELDUNG_ID AND (
                    E2.EXPORT_ID IS NOT NULL
                    OR E2.AUDIT_CREATED_AT > E1.AUDIT_CREATED_AT
                )
          )
    """
    )
    @Modifying
    suspend fun deduplicate(betriebsnummer: String, duplicatesExportId: Long = 1): Int

    /**
     * Weist alle noch nicht exportierten Entgeltbescheinigungen einer Betriebsnummer einer [exportId] zu.
     *
     * @param betriebsnummer Die Betriebsnummer des Arbeitgebers.
     * @param exportId Die ID des Ziel-Exports.
     * @return Die Anzahl der zugewiesenen Meldungen.
     */
    @Query(
        """
        UPDATE ENTGELTBESCHEINIGUNG_AU
        SET EXPORT_ID = :exportId
        WHERE ARBEITGEBER_BETRIEBSNUMMER = :betriebsnummer
        AND EXPORT_ID IS NULL
    """
    )
    @Modifying
    suspend fun assignToExport(betriebsnummer: String, exportId: Long): Int

    /**
     * Findet alle Entgeltbescheinigungen, die einer bestimmten Export-ID zugewiesen sind.
     *
     * @param exportId Die ID des Exports.
     * @return Ein [Flow] von [EntgeltbescheinigungAuEntity].
     */
    fun findByExportId(exportId: Long): Flow<EntgeltbescheinigungAuEntity>
}
