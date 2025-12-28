package coding.challenge.meldecenter.eingehend.shared

/**
 * Kontaktinformationen des Mitarbeiters.
 * Beispiel: KontaktDto(email = "max.mustermann@beispiel.de", ...)
 */
data class KontaktDto(
    val email: String,
    val telefon: TelefonDto? = null,
    val telefonnummer: String? = null,
    val bevorzugteKontaktsart: String? = null
)
