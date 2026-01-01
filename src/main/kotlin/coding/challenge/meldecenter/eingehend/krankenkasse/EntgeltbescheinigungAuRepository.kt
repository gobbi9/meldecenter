package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.ausgehend.krankenkasse.export.EntgeltbescheinigungAuExportGroup
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repository für [EntgeltbescheinigungAuEntity].
 */
@Repository
interface EntgeltbescheinigungAuRepository :
    CoroutineCrudRepository<EntgeltbescheinigungAuEntity, UUID>,
    CoroutineSortingRepository<EntgeltbescheinigungAuEntity, UUID> {

    fun findAllBy(pageable: Pageable): Flow<EntgeltbescheinigungAuEntity>

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
     * Duplizierte Entgeltbescheinigungen werden ignoriert,
     * und dem "Duplicates Export" (ID = 1) zugewiesen.
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
     * Weist Entgeltbescheinigungen einem Export zu.
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

    fun findByExportId(exportId: Long): Flow<EntgeltbescheinigungAuEntity>
}
