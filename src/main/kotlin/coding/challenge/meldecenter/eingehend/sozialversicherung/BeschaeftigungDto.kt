package coding.challenge.meldecenter.eingehend.sozialversicherung

import java.time.LocalDate

/**
 * Informationen zur Beschäftigung.
 * Beispiel: BeschaeftigungDto(beginn = LocalDate.now(), ...)
 */
data class BeschaeftigungDto(
    val beginn: LocalDate,
    val beschaeftigungsart: String
)
