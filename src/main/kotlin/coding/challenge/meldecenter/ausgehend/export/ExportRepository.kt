package coding.challenge.meldecenter.ausgehend.export

import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

/**
 * Repository für den Zugriff auf [ExportEntity]-Daten in der Datenbank.
 */
interface ExportRepository : CoroutineCrudRepository<ExportEntity, Long> {
    /**
     * Aktualisiert den Status eines Exports atomar, falls der aktuelle Status übereinstimmt.
     *
     * @param id Die ID des Exports.
     * @param newStatus Der neue zu setzende Status.
     * @param currentStatus Der erwartete aktuelle Status.
     * @return Die Anzahl der betroffenen Zeilen (1 bei Erfolg, 0 sonst).
     */
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

    /**
     * Sucht einen Export anhand seiner ID und seines Status.
     *
     * @param id Die ID des Exports.
     * @param status Der Status des Exports.
     * @return Die gefundene [ExportEntity] oder `null`.
     */
    suspend fun findByIdAndStatus(id: Long, status: ExportStatus): ExportEntity?
}
