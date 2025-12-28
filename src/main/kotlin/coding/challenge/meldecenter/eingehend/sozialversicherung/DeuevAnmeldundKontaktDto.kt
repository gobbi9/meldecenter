package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.eingehend.shared.TelefonDto

/**
 * Kontaktinformationen des Mitarbeiters.
 * Beispiel: DeuevAnmeldundKontaktDto(email = "max.mustermann@beispiel.de", ...)
 */
data class DeuevAnmeldundKontaktDto(
    val email: String,
    val telefon: TelefonDto,
)
