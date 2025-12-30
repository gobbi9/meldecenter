package coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.outbox

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlElementWrapper
import jakarta.xml.bind.annotation.XmlRootElement

/**
 * DTO für Rückmeldungen der Krankenkasse.
 */
@XmlRootElement(name = "Rueckmeldung")
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungDto(
    @field:XmlElement(name = "Kopf")
    val kopf: RueckmeldungKopfDto,

    @field:XmlElementWrapper(name = "Ergebnisse")
    @field:XmlElement(name = "Ergebnis")
    val ergebnisse: List<RueckmeldungErgebnisDto>
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
 * Einzelergebnis einer Meldungsverarbeitung bei der Krankenkasse.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungErgebnisDto(
    @field:XmlElement(name = "Referenz")
    val referenz: RueckmeldungReferenzDto,
    @field:XmlElement(name = "Status")
    val status: String
)

/**
 * Referenz auf die ursprüngliche Krankenkassenmeldung.
 */
@XmlAccessorType(XmlAccessType.FIELD)
data class RueckmeldungReferenzDto(
    @field:XmlElement(name = "RequestId")
    val requestId: String,
    @field:XmlElement(name = "MitarbeiterId")
    val mitarbeiterId: String
)
