package coding.challenge.meldecenter.eingehend.sozialversicherung

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Table("DEUEV_ANMELDUNG")
data class DeuevAnmeldungEntity(
    @Id val meldecenterId: UUID,
    val meldungId: UUID,
    val meldungTyp: String,
    val meldungQuelle: String,
    val meldungMandantId: String,
    val meldungErstelltAm: Instant,
    val arbeitgeberBetriebsnummer: String,
    val mitarbeiterId: String,
    val mitarbeiterVorname: String,
    val mitarbeiterNachname: String,
    val mitarbeiterGeburtsdatum: LocalDate,
    val mitarbeiterSozialversicherungsnummer: String,
    val mitarbeiterAnschriftStrasse: String,
    val mitarbeiterAnschriftPlz: String,
    val mitarbeiterAnschriftOrt: String,
    val mitarbeiterKontaktEmail: String,
    val mitarbeiterKontaktTelefonPraefix: String,
    val mitarbeiterKontaktTelefonVorwahl: String,
    val mitarbeiterKontaktTelefonNummer: String,
    val beschaeftigungBeginn: LocalDate,
    val beschaeftigungBeschaeftigungsart: String,
    val auditCreatedAt: Instant,
    val auditCreatedBy: String?,
    var auditUpdatedAt: Instant?,
    var auditUpdatedBy: String?,
)
