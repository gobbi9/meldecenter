package coding.challenge.meldecenter.eingehend.shared.meldungen

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant
import java.util.UUID

/** Eine generische Meldung */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
sealed interface Meldung {
    val meldung: MeldungDto
}

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

/** Aktuelle Meldungstypen */
enum class MeldungTyp {
    /** Entgeltbescheinigung Arbeitsunfähigkeit */
    ENTGELTBESCHEINIGUNG_KG,

    /** DEÜV-Anmeldung */
    DEUEV_ANMELDUNG,
}
