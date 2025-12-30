package coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.error

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

/**
 * DTO für technische Fehler bei der Rückmeldung.
 */
@XmlRootElement(name = "Rueckmeldung")
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungDto(
    @field:XmlElement(name = "Kopf")
    val kopf: RueckmeldungKopfDto,

    @field:XmlElement(name = "Status")
    val status: String,

    @field:XmlElement(name = "Fehler")
    val fehler: RueckmeldungFehlerDto
)

/**
 * Kopfdaten der Rückmeldung.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungKopfDto(
    @field:XmlElement(name = "Empfangszeitpunkt")
    val empfangszeitpunkt: String,
    @field:XmlElement(name = "Empfaenger")
    val empfaenger: String
)

/**
 * Fehlerdetails bei technischem Fehler.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungFehlerDto(
    @field:XmlElement(name = "Code")
    val code: String,
    @field:XmlElement(name = "Beschreibung")
    val beschreibung: String
)
