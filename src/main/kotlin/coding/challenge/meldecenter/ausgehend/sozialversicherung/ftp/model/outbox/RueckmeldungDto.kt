package coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.outbox

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlElementWrapper
import jakarta.xml.bind.annotation.XmlRootElement

/**
 * DTO für Rückmeldungen der Behörde.
 */
@XmlRootElement(name = "Rueckmeldung")
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungDto(
    @field:XmlElement(name = "Kopf")
    val kopf: RueckmeldungKopfDto,

    @field:XmlElement(name = "Status")
    val status: String?,

    @field:XmlElementWrapper(name = "Ergebnisse")
    @field:XmlElement(name = "Ergebnis")
    val ergebnisse: List<RueckmeldungErgebnisDto>?,

    @field:XmlElement(name = "Fehler")
    val fehler: RueckmeldungFehlerDto?
)

/**
 * Kopfdaten der Rückmeldung.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungKopfDto(
    @field:XmlElement(name = "Empfangszeitpunkt")
    val empfangszeitpunkt: String,
    @field:XmlElement(name = "Empfaenger")
    val empfaenger: String,
    @field:XmlElement(name = "Verfahren")
    val verfahren: String
)

/**
 * Einzelergebnis einer Meldungsverarbeitung.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungErgebnisDto(
    @field:XmlElement(name = "Referenz")
    val referenz: RueckmeldungReferenzDto,
    @field:XmlElement(name = "Status")
    val status: String,
    @field:XmlElement(name = "Hinweis")
    val hinweis: String?,
    @field:XmlElement(name = "Fehler")
    val fehler: RueckmeldungFehlerDto?
)

/**
 * Referenz auf die ursprüngliche Meldung.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungReferenzDto(
    @field:XmlElement(name = "RequestId")
    val requestId: String,
    @field:XmlElement(name = "MitarbeiterId")
    val mitarbeiterId: String
)

/**
 * Fehlerdetails in der Rückmeldung.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungFehlerDto(
    @field:XmlElement(name = "Code")
    val code: String,
    @field:XmlElement(name = "Kategorie")
    val kategorie: String,
    @field:XmlElement(name = "Beschreibung")
    val beschreibung: String,
    @field:XmlElement(name = "FehlerhaftesFeld")
    val fehlerhaftesFeld: String?
)
