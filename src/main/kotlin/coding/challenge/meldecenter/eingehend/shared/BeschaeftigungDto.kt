package coding.challenge.meldecenter.eingehend.shared

import java.time.LocalDate

/**
 * Informationen zur Beschäftigung.
 * Beispiel: BeschaeftigungDto(beginn = LocalDate.now(), ...)
 */
data class BeschaeftigungDto(
    val beginn: LocalDate,
    val beschaeftigungsart: String
)
