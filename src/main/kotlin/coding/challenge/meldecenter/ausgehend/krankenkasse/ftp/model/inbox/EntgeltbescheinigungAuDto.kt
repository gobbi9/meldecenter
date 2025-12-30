package coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlElementWrapper
import jakarta.xml.bind.annotation.XmlRootElement

/**
 * Sammel-DTO für Entgeltbescheinigungen bei Arbeitsunfähigkeit.
 */
@XmlRootElement(name = "EntgeltbescheinigungenArbeitsunfaehigkeit")
@XmlAccessorType(XmlAccessType.FIELD)
data class EntgeltbescheinigungenAuDto(
    @field:XmlElement(name = "Absender")
    val absender: KgAbsenderDto,

    @field:XmlElementWrapper(name = "Bescheinigungen")
    @field:XmlElement(name = "Bescheinigung")
    val bescheinigungen: List<KgBescheinigungDto>
)

/**
 * Informationen über den Absender der Bescheinigungen.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class KgAbsenderDto(
    @field:XmlElement(name = "Betriebsnummer")
    val betriebsnummer: String,
    @field:XmlElement(name = "Quelle")
    val quelle: String,
    @field:XmlElement(name = "Erstellungszeitpunkt")
    val erstellungszeitpunkt: String
)

/**
 * Einzelne Entgeltbescheinigung für einen Mitarbeiter.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class KgBescheinigungDto(
    @field:XmlElement(name = "ReferenzId")
    val referenzId: String,
    @field:XmlElement(name = "MitarbeiterId")
    val mitarbeiterId: String,
    @field:XmlElement(name = "Person")
    val person: KgPersonDto,
    @field:XmlElement(name = "Arbeitsunfaehigkeit")
    val arbeitsunfaehigkeit: KgArbeitsunfaehigkeitDto,
    @field:XmlElement(name = "Entgelt")
    val entgelt: KgEntgeltDto
)

/**
 * Persönliche Daten des Mitarbeiters in der Krankenkassenmeldung.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class KgPersonDto(
    @field:XmlElement(name = "Vorname")
    val vorname: String,
    @field:XmlElement(name = "Nachname")
    val nachname: String,
    @field:XmlElement(name = "Geburtsdatum")
    val geburtsdatum: String,
    @field:XmlElement(name = "Sozialversicherungsnummer")
    val sozialversicherungsnummer: String,
    @field:XmlElement(name = "Kontakt")
    val kontakt: KgKontaktDto?
)

/**
 * Kontaktinformationen des Mitarbeiters.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class KgKontaktDto(
    @field:XmlElement(name = "Telefon")
    val telefon: String?
)

/**
 * Zeitraum der Arbeitsunfähigkeit.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class KgArbeitsunfaehigkeitDto(
    @field:XmlElement(name = "Beginn")
    val beginn: String,
    @field:XmlElement(name = "Ende")
    val ende: String
)

/**
 * Entgeltdaten für die Bescheinigung.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class KgEntgeltDto(
    @field:XmlElement(name = "Bezugszeitraum")
    val bezugszeitraum: String,
    @field:XmlElement(name = "Bruttoentgelt")
    val bruttoentgelt: Double
)
