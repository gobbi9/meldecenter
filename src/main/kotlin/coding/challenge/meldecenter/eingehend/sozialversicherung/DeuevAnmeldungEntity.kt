package coding.challenge.meldecenter.eingehend.sozialversicherung

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

/**
 * Entity für die DEÜV-Anmeldung in der Datenbank.
 * Beispiel: DeuevAnmeldungEntity(meldungId = UUID.randomUUID(), ...)
 */
@Table("DEUEV_ANMELDUNG")
data class DeuevAnmeldungEntity(
    /** Interne ID des Meldecenters. Beispiel: UUID.randomUUID() */
    @Id val meldecenterId: UUID = UUID.randomUUID(),
    /** ID der ursprünglichen Meldung. Beispiel: UUID.randomUUID() */
    val meldungId: UUID,
    /** Typ der Meldung. Beispiel: "DEUEV_ANMELDUNG" */
    val meldungTyp: String,
    /** Quelle der Meldung. Beispiel: "EAP_SAP_HCM" */
    val meldungQuelle: String,
    /** ID des Mandanten. Beispiel: "MANDANT_4711" */
    val meldungMandantId: String,
    /** Zeitpunkt der Erstellung. Beispiel: Instant.now() */
    val meldungErstelltAm: Instant,
    /** Betriebsnummer des Arbeitgebers. Beispiel: "12345678" */
    val arbeitgeberBetriebsnummer: String,
    /** ID des Mitarbeiters. Beispiel: "EMP-9981" */
    val mitarbeiterId: String,
    /** Vorname des Mitarbeiters. Beispiel: "Max" */
    val mitarbeiterVorname: String,
    /** Nachname des Mitarbeiters. Beispiel: "Mustermann" */
    val mitarbeiterNachname: String,
    /** Geburtsdatum des Mitarbeiters. Beispiel: LocalDate.of(1990, 4, 12) */
    val mitarbeiterGeburtsdatum: LocalDate,
    /** Sozialversicherungsnummer des Mitarbeiters. Beispiel: "65 120490 M 123" */
    val mitarbeiterSozialversicherungsnummer: String,
    /** Straße der Anschrift. Beispiel: "Hauptstraße 10" */
    val mitarbeiterAnschriftStrasse: String,
    /** PLZ der Anschrift. Beispiel: "10115" */
    val mitarbeiterAnschriftPlz: String,
    /** Ort der Anschrift. Beispiel: "Berlin" */
    val mitarbeiterAnschriftOrt: String,
    /** E-Mail des Kontakts. Beispiel: "max.mustermann@beispiel.de" */
    val mitarbeiterKontaktEmail: String,
    /** Präfix der Telefonnummer. Beispiel: "+49" */
    val mitarbeiterKontaktTelefonPraefix: String,
    /** Vorwahl der Telefonnummer. Beispiel: "30" */
    val mitarbeiterKontaktTelefonVorwahl: String,
    /** Nummer der Telefonnummer. Beispiel: "1234567" */
    val mitarbeiterKontaktTelefonNummer: String,
    /** Beginn der Beschäftigung. Beispiel: LocalDate.of(2025, 2, 1) */
    val beschaeftigungBeginn: LocalDate,
    /** Art der Beschäftigung. Beispiel: "SOZIALVERSICHERUNGSPFLICHTIG" */
    val beschaeftigungBeschaeftigungsart: String,
    /** Zeitpunkt der Erstellung im System. Beispiel: Instant.now() */
    val auditCreatedAt: Instant = Instant.now(),
    /** Ersteller des Datensatzes. Beispiel: "system" */
    val auditCreatedBy: String? = null,
    /** Zeitpunkt der letzten Aktualisierung. Beispiel: Instant.now() */
    var auditUpdatedAt: Instant? = null,
    /** Letzter Bearbeiter des Datensatzes. Beispiel: "admin" */
    var auditUpdatedBy: String? = null,
    /** Versionsnummer für optimistisches Locking.
     * Spring braucht das, um bei .save zwischen
     * einem INSERT und UPDATE zu unterscheiden, da PK ist eine UUID und wird
     * bei der Instanziierung erstellt. */
    @Version val version: Long? = null,
)
