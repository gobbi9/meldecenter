package coding.challenge.meldecenter.ausgehend.export.event

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

/**
 * Entity für ein Export-Event in der Datenbank.
 */
@Table("EXPORT_EVENT")
data class ExportEventEntity(
    /** Die ID des Export-Events. */
    @Id
    val id: UUID = UUID.randomUUID(),

    /** Die ID des zugehörigen Exports. */
    val exportId: Long,

    /** Der Typ des Events. */
    val type: ExportEventType,

    /** Details zum Event. */
    val details: String? = null,

    /** Der Zeitpunkt der Erstellung. */
    val createdAt: LocalDateTime = LocalDateTime.now(),

    /** Der Ersteller des Datensatzes. */
    val createdBy: String,

    /** Versionsnummer für optimistisches Locking. */
    @Version
    val version: Long? = null
)
