package coding.challenge.meldecenter.eingehend.shared.meldungen

import coding.challenge.meldecenter.eingehend.shared.ArbeitgeberDto
import coding.challenge.meldecenter.eingehend.shared.BeschaeftigungDto
import coding.challenge.meldecenter.eingehend.shared.MitarbeiterDto
import java.util.UUID

/**
 * Datenübertragungsobjekt für eine DEÜV-Anmeldung.
 * Beispiel: DeuvAnmeldungDto(meldung = MeldungDto(...), ...)
 */
data class DeuvAnmeldungDto(
    val meldecenterId: UUID? = null,
    override val meldung: MeldungDto,
    val arbeitgeber: ArbeitgeberDto,
    val mitarbeiter: MitarbeiterDto,
    val beschaeftigung: BeschaeftigungDto
) : Meldung

