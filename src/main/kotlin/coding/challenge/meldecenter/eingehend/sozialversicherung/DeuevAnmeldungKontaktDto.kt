package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.eingehend.shared.TelefonDto

/**
 * Kontaktinformationen des Mitarbeiters.
 * Beispiel: DeuevAnmeldungKontaktDto(email = "max.mustermann@beispiel.de", ...)
 */
data class DeuevAnmeldungKontaktDto(
    val email: String,
    val telefon: TelefonDto,
)
