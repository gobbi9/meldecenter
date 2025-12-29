package coding.challenge.meldecenter.ausgehend.export

import coding.challenge.meldecenter.eingehend.shared.MeldungTyp
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

/**
 * Entity für den Export in der Datenbank.
 */
@Table("EXPORT")
data class ExportEntity(
    /** Die ID des Exports. */
    @Id
    val id: Long = 0,

    /** Der Typ der Meldung. */
    val typ: MeldungTyp,

    /** Der Status des Exports. */
    val status: ExportStatus,

    /** Der Zeitpunkt der Erstellung. */
    val createdAt: Instant = Instant.now(),

    /** Der Ersteller des Datensatzes. */
    val createdBy: String? = null,

    /** Der Zeitpunkt der letzten Aktualisierung. */
    var updatedAt: Instant? = null,

    /** Der letzte Bearbeiter des Datensatzes. */
    var updatedBy: String? = null
)
