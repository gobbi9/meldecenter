package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.eingehend.shared.TelefonDto

/**
 * Kontaktinformationen des Mitarbeiters.
 * Beispiel: DeuvAnmeldundKontaktDto(email = "max.mustermann@beispiel.de", ...)
 */
data class DeuvAnmeldundKontaktDto(
    val email: String,
    val telefon: TelefonDto,
)
