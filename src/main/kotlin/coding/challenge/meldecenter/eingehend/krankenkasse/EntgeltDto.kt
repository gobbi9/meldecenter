package coding.challenge.meldecenter.eingehend.krankenkasse

import java.math.BigDecimal

/**
 * Informationen zum Entgelt.
 */
data class EntgeltDto(
    val bezugszeitraum: String,
    val bruttoentgelt: BigDecimal,
)
