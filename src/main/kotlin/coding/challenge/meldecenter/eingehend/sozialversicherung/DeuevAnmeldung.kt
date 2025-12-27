package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.shared.entity.Audit
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Table
data class DeuevAnmeldung(
    @Id val meldecenterId: UUID,
    @Embedded.Nullable val meldung: Meldung,
    @Embedded.Nullable val arbeitgeber: Arbeitgeber,
    @Embedded.Nullable val mitarbeiter: Mitarbeiter,
    @Embedded.Nullable val beschaeftigung: Beschaeftigung,
    @Embedded.Nullable val audit: Audit,
)

data class Meldung(
    val id: UUID,
    val typ: String,
    val quelle: String,
    val mandantId: String,
    val erstelltAm: Instant,
)

data class Arbeitgeber(
    val betriebsnummer: String,
)

data class Mitarbeiter(
    val id: String,
    val vorname: String,
    val nachname: String,
    val geburtsdatum: LocalDate,
    val sozialversicherungsnummer: String,
    @Embedded.Nullable val anschrift: Anschrift,
    @Embedded.Nullable val kontakt: Kontakt,
)

data class Anschrift(
    val strasse: String,
    val plz: String,
    val ort: String,
)

data class Kontakt(
    val email: String,
    @Embedded.Nullable val telefon: Telefon,
)

data class Telefon(
    val praefix: String,
    val vorwahl: String,
    val nummer: String,
)

data class Beschaeftigung(
    val beginn: LocalDate,
    val beschaeftigungsart: String,
)
