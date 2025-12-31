package coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlElementWrapper
import jakarta.xml.bind.annotation.XmlRootElement

/**
 * Sammel-DTO für DEÜV-Anmeldungen.
 */
@XmlRootElement(name = "DEUEVAnmeldungen")
@XmlAccessorType(XmlAccessType.FIELD)
data class DeuevAnmeldungenDto(
    @field:XmlElement(name = "Absender")
    val absender: DeuevAbsenderDto,

    @field:XmlElementWrapper(name = "Anmeldungen")
    @field:XmlElement(name = "Anmeldung")
    val anmeldungen: List<DeuevAnmeldungDto>
)

/**
 * Informationen über den Absender der Meldung.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class DeuevAbsenderDto(
    @field:XmlElement(name = "Betriebsnummer")
    val betriebsnummer: String,
    @field:XmlElement(name = "Erstellungszeitpunkt")
    val erstellungszeitpunkt: String
)

/**
 * Einzelne DEÜV-Anmeldung eines Mitarbeiters.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class DeuevAnmeldungDto(
    @field:XmlElement(name = "ReferenzId")
    val referenzId: String,
    @field:XmlElement(name = "MitarbeiterId")
    val mitarbeiterId: String,
    @field:XmlElement(name = "Person")
    val person: DeuevPersonDto,
    @field:XmlElement(name = "Beschaeftigung")
    val beschaeftigung: DeuevBeschaeftigungDto
)

/**
 * Persönliche Daten des Mitarbeiters.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class DeuevPersonDto(
    @field:XmlElement(name = "Vorname")
    val vorname: String,
    @field:XmlElement(name = "Nachname")
    val nachname: String,
    @field:XmlElement(name = "Geburtsdatum")
    val geburtsdatum: String,
    @field:XmlElement(name = "Sozialversicherungsnummer")
    val sozialversicherungsnummer: String,
    @field:XmlElement(name = "Anschrift")
    val anschrift: DeuevAnschriftDto,
    @field:XmlElement(name = "Kontakt")
    val kontakt: DeuevKontaktDto?
)

/**
 * Anschrift des Mitarbeiters.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class DeuevAnschriftDto(
    @field:XmlElement(name = "Strasse")
    val strasse: String,
    @field:XmlElement(name = "Postleitzahl")
    val postleitzahl: String,
    @field:XmlElement(name = "Ort")
    val ort: String
)

/**
 * Kontaktinformationen des Mitarbeiters.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class DeuevKontaktDto(
    @field:XmlElement(name = "Email")
    val email: String?,
    @field:XmlElement(name = "Telefon")
    val telefon: String?
)

/**
 * Informationen zum Beschäftigungsverhältnis.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class DeuevBeschaeftigungDto(
    @field:XmlElement(name = "Beginn")
    val beginn: String,
    @field:XmlElement(name = "Art")
    val art: String
)
