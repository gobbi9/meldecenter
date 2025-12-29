package coding.challenge.meldecenter.ausgehend.export.event

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

/**
 * Repository für den Zugriff auf Export-Events.
 */
interface ExportEventRepository : CoroutineCrudRepository<ExportEventEntity, UUID>
