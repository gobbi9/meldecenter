package coding.challenge.meldecenter.eingehend.krankenkasse

import java.time.LocalDate

/**
 * Informationen zur Krankheit/Arbeitsunfähigkeit.
 */
data class KrankheitDto(
    val arbeitsunfaehigkeitBeginn: LocalDate,
    val arbeitsunfaehigkeitEnde: LocalDate
)
