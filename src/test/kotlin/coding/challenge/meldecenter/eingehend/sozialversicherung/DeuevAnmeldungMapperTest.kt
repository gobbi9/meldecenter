package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.eingehend.shared.AnschriftDto
import coding.challenge.meldecenter.eingehend.shared.ArbeitgeberDto
import coding.challenge.meldecenter.eingehend.shared.MeldungDto
import coding.challenge.meldecenter.eingehend.shared.MitarbeiterDto
import coding.challenge.meldecenter.eingehend.shared.TelefonDto
import coding.challenge.meldecenter.eingehend.shared.meldungen.DeuevAnmeldungDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.time.LocalDate
import java.util.UUID

/**
 * Test für [DeuevAnmeldungDto.toEntity()] und [DeuevAnmeldungEntity.toDto()].
 */
class DeuevAnmeldungMapperTest : StringSpec({

    val meldecenterId = UUID.randomUUID()
    val meldungId = UUID.randomUUID()
    val erstelltAm = LocalDateTime.parse("2025-12-28T10:00:00")
    val geburtsdatum = LocalDate.of(1990, 1, 1)
    val beginn = LocalDate.of(2025, 1, 1)

    val dto = DeuevAnmeldungDto(
        meldecenterId = meldecenterId,
        meldung = MeldungDto(
            id = meldungId,
            typ = "DEUEV_ANMELDUNG",
            quelle = "TEST_QUELLE",
            mandantId = "MANDANT_1",
            erstelltAm = erstelltAm
        ),
        arbeitgeber = ArbeitgeberDto(
            betriebsnummer = "12345678"
        ),
        mitarbeiter = MitarbeiterDto(
            id = "EMP_1",
            vorname = "Max",
            nachname = "Mustermann",
            geburtsdatum = geburtsdatum,
            sozialversicherungsnummer = "12345678M001",
            anschrift = AnschriftDto(
                strasse = "Musterstraße 1",
                plz = "12345",
                ort = "Musterstadt"
            ),
            kontakt = DeuevAnmeldungKontaktDto(
                email = "max@mustermann.de",
                telefon = TelefonDto(
                    praefix = "+49",
                    vorwahl = "123",
                    nummer = "456789"
                )
            )
        ),
        beschaeftigung = BeschaeftigungDto(
            beginn = beginn,
            beschaeftigungsart = "VOLLZEIT"
        )
    )

    val entity = DeuevAnmeldungEntity(
        meldecenterId = meldecenterId,
        meldungId = meldungId,
        meldungTyp = "DEUEV_ANMELDUNG",
        meldungQuelle = "TEST_QUELLE",
        meldungMandantId = "MANDANT_1",
        meldungErstelltAm = erstelltAm,
        arbeitgeberBetriebsnummer = "12345678",
        mitarbeiterId = "EMP_1",
        mitarbeiterVorname = "Max",
        mitarbeiterNachname = "Mustermann",
        mitarbeiterGeburtsdatum = geburtsdatum,
        mitarbeiterSozialversicherungsnummer = "12345678M001",
        mitarbeiterAnschriftStrasse = "Musterstraße 1",
        mitarbeiterAnschriftPlz = "12345",
        mitarbeiterAnschriftOrt = "Musterstadt",
        mitarbeiterKontaktEmail = "max@mustermann.de",
        mitarbeiterKontaktTelefonPraefix = "+49",
        mitarbeiterKontaktTelefonVorwahl = "123",
        mitarbeiterKontaktTelefonNummer = "456789",
        beschaeftigungBeginn = beginn,
        beschaeftigungBeschaeftigungsart = "VOLLZEIT"
    )

    "Map DeuevAnmeldungDto to DeuevAnmeldungEntity" {
        val result = dto.toEntity()

        result.meldecenterId.shouldNotBeNull()
        result.meldungId shouldBe dto.meldung.id
        result.meldungTyp shouldBe dto.meldung.typ
        result.meldungQuelle shouldBe dto.meldung.quelle
        result.meldungMandantId shouldBe dto.meldung.mandantId
        result.meldungErstelltAm shouldBe dto.meldung.erstelltAm
        result.arbeitgeberBetriebsnummer shouldBe dto.arbeitgeber.betriebsnummer
        result.mitarbeiterId shouldBe dto.mitarbeiter.id
        result.mitarbeiterVorname shouldBe dto.mitarbeiter.vorname
        result.mitarbeiterNachname shouldBe dto.mitarbeiter.nachname
        result.mitarbeiterGeburtsdatum shouldBe dto.mitarbeiter.geburtsdatum
        result.mitarbeiterSozialversicherungsnummer shouldBe dto.mitarbeiter.sozialversicherungsnummer
        result.mitarbeiterAnschriftStrasse shouldBe dto.mitarbeiter.anschrift.strasse
        result.mitarbeiterAnschriftPlz shouldBe dto.mitarbeiter.anschrift.plz
        result.mitarbeiterAnschriftOrt shouldBe dto.mitarbeiter.anschrift.ort
        result.mitarbeiterKontaktEmail shouldBe dto.mitarbeiter.kontakt.email
        result.mitarbeiterKontaktTelefonPraefix shouldBe dto.mitarbeiter.kontakt.telefon.praefix
        result.mitarbeiterKontaktTelefonVorwahl shouldBe dto.mitarbeiter.kontakt.telefon.vorwahl
        result.mitarbeiterKontaktTelefonNummer shouldBe dto.mitarbeiter.kontakt.telefon.nummer
        result.beschaeftigungBeginn shouldBe dto.beschaeftigung.beginn
        result.beschaeftigungBeschaeftigungsart shouldBe dto.beschaeftigung.beschaeftigungsart
    }

    "Map DeuevAnmeldungEntity to DeuevAnmeldungDto" {
        val result = entity.toDto()

        result.meldecenterId shouldBe entity.meldecenterId
        result.meldung.id shouldBe entity.meldungId
        result.meldung.typ shouldBe entity.meldungTyp
        result.meldung.quelle shouldBe entity.meldungQuelle
        result.meldung.mandantId shouldBe entity.meldungMandantId
        result.meldung.erstelltAm shouldBe entity.meldungErstelltAm
        result.arbeitgeber.betriebsnummer shouldBe entity.arbeitgeberBetriebsnummer
        result.mitarbeiter.id shouldBe entity.mitarbeiterId
        result.mitarbeiter.vorname shouldBe entity.mitarbeiterVorname
        result.mitarbeiter.nachname shouldBe entity.mitarbeiterNachname
        result.mitarbeiter.geburtsdatum shouldBe entity.mitarbeiterGeburtsdatum
        result.mitarbeiter.sozialversicherungsnummer shouldBe entity.mitarbeiterSozialversicherungsnummer
        result.mitarbeiter.anschrift.strasse shouldBe entity.mitarbeiterAnschriftStrasse
        result.mitarbeiter.anschrift.plz shouldBe entity.mitarbeiterAnschriftPlz
        result.mitarbeiter.anschrift.ort shouldBe entity.mitarbeiterAnschriftOrt
        result.mitarbeiter.kontakt.email shouldBe entity.mitarbeiterKontaktEmail
        result.mitarbeiter.kontakt.telefon.praefix shouldBe entity.mitarbeiterKontaktTelefonPraefix
        result.mitarbeiter.kontakt.telefon.vorwahl shouldBe entity.mitarbeiterKontaktTelefonVorwahl
        result.mitarbeiter.kontakt.telefon.nummer shouldBe entity.mitarbeiterKontaktTelefonNummer
        result.beschaeftigung.beginn shouldBe entity.beschaeftigungBeginn
        result.beschaeftigung.beschaeftigungsart shouldBe entity.beschaeftigungBeschaeftigungsart
    }
})
