package coding.challenge.meldecenter.ausgehend.export

import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

/**
 * Repository für den Zugriff auf Exports.
 */
interface ExportRepository : CoroutineCrudRepository<ExportEntity, Long> {
    @Query("""
        UPDATE EXPORT
        SET STATUS = :newStatus
        WHERE ID = :id
        AND STATUS = :currentStatus
    """)
    @Modifying
    suspend fun updateStatus(
        id: Long,
        newStatus: ExportStatus,
        currentStatus: ExportStatus,
    ): Int

    suspend fun findByIdAndStatus(id: Long, status: ExportStatus): ExportEntity?
}
