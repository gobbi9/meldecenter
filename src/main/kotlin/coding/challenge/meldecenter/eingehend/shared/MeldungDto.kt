package coding.challenge.meldecenter.eingehend.shared

import java.time.Instant
import java.util.UUID

/**
 * Metadaten der Meldung.
 * Beispiel: MeldungDto(id = UUID.randomUUID(), typ = "DEUEV_ANMELDUNG", ...)
 */
data class MeldungDto(
    val id: UUID,
    /** Siehe [MeldungTyp] */
    val typ: String,
    val quelle: String,
    val mandantId: String,
    val erstelltAm: Instant,
)
