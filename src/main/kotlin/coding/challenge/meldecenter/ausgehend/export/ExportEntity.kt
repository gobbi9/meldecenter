package coding.challenge.meldecenter.ausgehend.export

import coding.challenge.meldecenter.eingehend.shared.MeldungTyp
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

/**
 * Entity für den Export in der Datenbank.
 */
@Table("EXPORT")
data class ExportEntity(
    /** Die ID des Exports. */
    @Id
    val id: Long = 0,

    /** Der Typ der Meldung. Siehe [MeldungTyp] */
    val typ: String,

    /** Der Status des Exports. */
    var status: ExportStatus,

    /** Die Betriebsnummer des Arbeitgebers. */
    val betriebsnummer: String,

    /** Die Trace ID des Exports. */
    val traceId: String,

    /** Der Zeitpunkt der Erstellung. */
    val createdAt: LocalDateTime = LocalDateTime.now(),

    /** Der Ersteller des Datensatzes. */
    val createdBy: String? = null,

    /** Der Zeitpunkt der letzten Aktualisierung. */
    var updatedAt: LocalDateTime? = null,

    /** Der letzte Bearbeiter des Datensatzes. */
    var updatedBy: String? = null
)
