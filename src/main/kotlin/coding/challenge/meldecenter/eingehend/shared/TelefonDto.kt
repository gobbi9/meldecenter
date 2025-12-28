package coding.challenge.meldecenter.eingehend.shared

/**
 * Telefonnummer-Details.
 * Beispiel: TelefonDto(praefix = "+49", vorwahl = "30", nummer = "1234567")
 */
data class TelefonDto(
    val praefix: String,
    val vorwahl: String,
    val nummer: String
)
