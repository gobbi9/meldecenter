package coding.challenge.meldecenter.shared.krankenkasse

import coding.challenge.meldecenter.eingehend.shared.MeldungTyp
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

/**
 * Entity für die Entgeltbescheinigung bei Arbeitsunfähigkeit in der Datenbank.
 */
@Table("ENTGELTBESCHEINIGUNG_AU")
data class EntgeltbescheinigungAuEntity(
    /** Interne ID des Meldecenters. */
    @Id val meldecenterId: UUID = UUID.randomUUID(),
    /** ID der ursprünglichen Meldung. */
    val meldungId: UUID,
    /** Typ der Meldung. Siehe [MeldungTyp] */
    val meldungTyp: String,
    /** Quelle der Meldung. */
    val meldungQuelle: String,
    /** ID des Mandanten. */
    val meldungMandantId: String,
    /** Zeitpunkt der Erstellung. */
    val meldungErstelltAm: LocalDateTime,
    /** Betriebsnummer des Arbeitgebers. */
    val arbeitgeberBetriebsnummer: String,
    /** ID des Mitarbeiters. */
    val mitarbeiterId: String,
    /** Vorname des Mitarbeiters. */
    val mitarbeiterVorname: String,
    /** Nachname des Mitarbeiters. */
    val mitarbeiterNachname: String,
    /** Geburtsdatum des Mitarbeiters. */
    val mitarbeiterGeburtsdatum: LocalDate,
    /** Sozialversicherungsnummer des Mitarbeiters. */
    val mitarbeiterSozialversicherungsnummer: String,
    /** Straße der Anschrift. */
    val mitarbeiterAnschriftStrasse: String,
    /** PLZ der Anschrift. */
    val mitarbeiterAnschriftPlz: String,
    /** Ort der Anschrift. */
    val mitarbeiterAnschriftOrt: String,
    /** E-Mail des Kontakts. */
    val mitarbeiterKontaktEmail: String,
    /** Telefonnummer des Kontakts. */
    val mitarbeiterKontaktTelefonnummer: String,
    /** Bevorzugte Kontaktart des Kontakts. */
    val mitarbeiterKontaktBevorzugteKontaktsart: String,
    /** Beginn der Arbeitsunfähigkeit. */
    val krankheitArbeitsunfaehigkeitBeginn: LocalDate,
    /** Ende der Arbeitsunfähigkeit. */
    val krankheitArbeitsunfaehigkeitEnde: LocalDate,
    /** Bezugszeitraum des Entgelts. */
    val entgeltBezugszeitraum: String,
    /** Bruttoentgelt. */
    val entgeltBruttoentgelt: BigDecimal,
    /** Zeitpunkt der Erstellung im System. */
    val auditCreatedAt: LocalDateTime = LocalDateTime.now(),
    /** Ersteller des Datensatzes. */
    val auditCreatedBy: String? = null,
    /** Zeitpunkt der letzten Aktualisierung. */
    var auditUpdatedAt: LocalDateTime? = null,
    /** Letzter Bearbeiter des Datensatzes. */
    var auditUpdatedBy: String? = null,
    /** ID des zugehörigen Exports. */
    val exportId: Long? = null,
    /** Versionsnummer für optimistisches Locking. */
    @Version val version: Long? = null,
)
