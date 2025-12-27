package coding.challenge.meldecenter.eingehend.sozialversicherung

import java.time.Instant
import java.time.LocalDate
import java.util.UUID

/**
 * Datenübertragungsobjekt für eine DEÜV-Anmeldung.
 * Beispiel: DeuvAnmeldungDto(meldung = MeldungDto(...), ...)
 */
data class DeuvAnmeldungDto(
    val meldecenterId: UUID? = null,
    val meldung: MeldungDto,
    val arbeitgeber: ArbeitgeberDto,
    val mitarbeiter: MitarbeiterDto,
    val beschaeftigung: BeschaeftigungDto
)

/**
 * Metadaten der Meldung.
 * Beispiel: MeldungDto(id = UUID.randomUUID(), typ = "DEUEV_ANMELDUNG", ...)
 */
data class MeldungDto(
    val id: UUID,
    val typ: String,
    val quelle: String,
    val mandantId: String,
    val erstelltAm: Instant
)

/**
 * Daten des Arbeitgebers.
 * Beispiel: ArbeitgeberDto(betriebsnummer = "12345678")
 */
data class ArbeitgeberDto(
    val betriebsnummer: String
)

/**
 * Daten des Mitarbeiters.
 * Beispiel: MitarbeiterDto(vorname = "Max", nachname = "Mustermann", ...)
 */
data class MitarbeiterDto(
    val id: String,
    val vorname: String,
    val nachname: String,
    val geburtsdatum: LocalDate,
    val sozialversicherungsnummer: String,
    val anschrift: AnschriftDto,
    val kontakt: KontaktDto
)

/**
 * Anschrift des Mitarbeiters.
 * Beispiel: AnschriftDto(strasse = "Hauptstraße 10", plz = "10115", ort = "Berlin")
 */
data class AnschriftDto(
    val strasse: String,
    val plz: String,
    val ort: String
)

/**
 * Kontaktinformationen des Mitarbeiters.
 * Beispiel: KontaktDto(email = "max.mustermann@beispiel.de", ...)
 */
data class KontaktDto(
    val email: String,
    val telefon: TelefonDto
)

/**
 * Telefonnummer-Details.
 * Beispiel: TelefonDto(praefix = "+49", vorwahl = "30", nummer = "1234567")
 */
data class TelefonDto(
    val praefix: String,
    val vorwahl: String,
    val nummer: String
)

/**
 * Informationen zur Beschäftigung.
 * Beispiel: BeschaeftigungDto(beginn = LocalDate.now(), ...)
 */
data class BeschaeftigungDto(
    val beginn: LocalDate,
    val beschaeftigungsart: String
)

