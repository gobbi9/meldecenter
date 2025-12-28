package coding.challenge.meldecenter.eingehend.shared

import java.math.BigDecimal

/**
 * Informationen zum Entgelt.
 */
data class EntgeltDto(
    val bezugszeitraum: String,
    val bruttoentgelt: BigDecimal
)
