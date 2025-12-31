package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.ausgehend.krankenkasse.export.EntgeltbescheinigungAuExportGroup
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

private val log = KotlinLogging.logger { }

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
        SELECT BETRIEBSNUMMER, COUNT(*) AS COUNT
        FROM ENTGELTBESCHEINIGUNG_AU
        GROUP BY BETRIEBSNUMMER
        """
    )
    fun findAllExportGroups(): Flow<EntgeltbescheinigungAuExportGroup>

    /**
     * Duplizierte Entgeltbescheinigungen werden ignoriert,
     * und dem "Duplicates Export" (ID = 1) zugewiesen.
     */
    @Query(
        """
        WITH DUPLICATES AS (
            SELECT MELDECENTER_ID FROM (
                SELECT MELDECENTER_ID,
                       ROW_NUMBER() OVER (
                            PARTITION BY MELDUNG_ID
                            ORDER BY CREATED_AT DESC
                       ) AS ROW_NUMBER
                FROM ENTGELTBESCHEINIGUNG_AU
            ) _
            WHERE ROW_NUMBER > 1
        )

        UPDATE ENTGELTBESCHEINIGUNG_AU
        SET EXPORT_ID = :duplicatesExportId
        WHERE EXPORT_ID IS NULL
        AND MELDECENTER_ID IN (DUPLICATES)
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
        WHERE BETRIEBSNUMMER = :betriebsnummer
        AND EXPORT_ID IS NULL
    """
    )
    @Modifying
    suspend fun assignToExport(betriebsnummer: String, exportId: Long): Int

    fun findByExportId(exportId: Long): Flow<EntgeltbescheinigungAuEntity>
}
