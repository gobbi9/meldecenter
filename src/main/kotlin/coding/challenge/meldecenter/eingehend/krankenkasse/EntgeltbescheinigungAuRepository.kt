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
        WITH EXPORTED AS (
            SELECT DISTINCT MELDUNG_ID
            FROM ENTGELTBESCHEINIGUNG_AU
            WHERE EXPORT_ID IS NOT NULL
        ),
        DUPLICATES AS (
            SELECT MELDECENTER_ID FROM (
                SELECT MELDECENTER_ID, MELDUNG_ID,
                       ROW_NUMBER() OVER (
                            PARTITION BY MELDUNG_ID
                            ORDER BY AUDIT_CREATED_AT DESC
                       ) AS ROW_NUMBER
                FROM ENTGELTBESCHEINIGUNG_AU
                WHERE MELDUNG_ID NOT IN (SELECT MELDUNG_ID FROM EXPORTED)
            ) _
            WHERE ROW_NUMBER > 1
            UNION
            SELECT MELDECENTER_ID
            FROM ENTGELTBESCHEINIGUNG_AU
            WHERE MELDUNG_ID IN (SELECT MELDUNG_ID FROM EXPORTED)
            AND EXPORT_ID IS NULL
        )

        UPDATE ENTGELTBESCHEINIGUNG_AU
        SET EXPORT_ID = :duplicatesExportId
        WHERE EXPORT_ID IS NULL
        AND MELDECENTER_ID IN (SELECT MELDECENTER_ID FROM DUPLICATES)
    """
    )
    @Modifying
    suspend fun deduplicate(duplicatesExportId: Long = 1): Int

    /**
     * Weist Entgeltbescheinigungen einem Export zu.
     */
    @Query(
        """
        UPDATE ENTGELTBESCHEINIGUNG_AU
        SET EXPORT_ID =  :exportId
        WHERE ARBEITGEBER_BETRIEBSNUMMER = :betriebsnummer
        AND EXPORT_ID IS NULL
    """
    )
    @Modifying
    suspend fun assignToExport(betriebsnummer: String, exportId: Long): Int

    fun findByExportId(exportId: Long): Flow<EntgeltbescheinigungAuEntity>
}
