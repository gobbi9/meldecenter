package coding.challenge.meldecenter.eingehend.shared.meldungen

import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltDto
import coding.challenge.meldecenter.eingehend.krankenkasse.KrankheitDto
import coding.challenge.meldecenter.eingehend.shared.ArbeitgeberDto
import coding.challenge.meldecenter.eingehend.shared.MeldungDto
import coding.challenge.meldecenter.eingehend.shared.MitarbeiterDto
import java.util.UUID

/**
 * Datenübertragungsobjekt für eine Entgeltbescheinigung bei Arbeitsunfähigkeit.
 */
data class EntgeltbescheinigungAuDto(
    val meldecenterId: UUID? = null,
    val meldung: MeldungDto,
    val arbeitgeber: ArbeitgeberDto,
    val mitarbeiter: MitarbeiterDto,
    val krankheit: KrankheitDto,
    val entgelt: EntgeltDto
) : Meldung
