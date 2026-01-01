package coding.challenge.meldecenter.ausgehend.export.event

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

/**
 * Repository für den Zugriff auf [ExportEventEntity]-Daten in der Datenbank.
 */
interface ExportEventRepository : CoroutineCrudRepository<ExportEventEntity, UUID>
