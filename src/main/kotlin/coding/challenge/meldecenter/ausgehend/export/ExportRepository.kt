package coding.challenge.meldecenter.ausgehend.export

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

/**
 * Repository für den Zugriff auf Exports.
 */
interface ExportRepository : CoroutineCrudRepository<ExportEntity, Long>
