package coding.challenge.meldecenter.eingehend.krankenkasse

/**
 * Kontaktinformationen des Mitarbeiters.
 * Beispiel: EntgeltbescheinigungAuKontaktDto(email = "max.mustermann@beispiel.de", ...)
 */
data class EntgeltbescheinigungAuKontaktDto(
    val email: String,
    val telefonnummer: String,
    val bevorzugteKontaktsart: String,
)
