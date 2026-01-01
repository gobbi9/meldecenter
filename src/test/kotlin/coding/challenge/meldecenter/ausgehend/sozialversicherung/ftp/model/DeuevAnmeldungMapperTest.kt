package coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportStatus
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevAnmeldungDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevAnschriftDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevBeschaeftigungDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevKontaktDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevPersonDto
import coding.challenge.meldecenter.shared.sozialversicherung.DeuevAnmeldungEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class DeuevAnmeldungMapperTest : StringSpec({

    "ToDeuevAnmeldungDto should map entity to DTO" {
        val meldungId = UUID.randomUUID()
        val mitarbeiterId = "EMP-123"
        val geburtsdatum = LocalDate.of(1990, 1, 1)
        val beginn = LocalDate.of(2025, 2, 1)

        val entity = DeuevAnmeldungEntity(
            meldungId = meldungId,
            meldungTyp = "DEUEV_ANMELDUNG",
            meldungQuelle = "EAP",
            meldungMandantId = "M1",
            meldungErstelltAm = LocalDateTime.now(),
            arbeitgeberBetriebsnummer = "12345678",
            mitarbeiterId = mitarbeiterId,
            mitarbeiterVorname = "Max",
            mitarbeiterNachname = "Mustermann",
            mitarbeiterGeburtsdatum = geburtsdatum,
            mitarbeiterSozialversicherungsnummer = "65 120490 M 123",
            mitarbeiterAnschriftStrasse = "Hauptstraße 10",
            mitarbeiterAnschriftPlz = "10115",
            mitarbeiterAnschriftOrt = "Berlin",
            mitarbeiterKontaktEmail = "max.mustermann@beispiel.de",
            mitarbeiterKontaktTelefonPraefix = "+49",
            mitarbeiterKontaktTelefonVorwahl = "30",
            mitarbeiterKontaktTelefonNummer = "1234567",
            beschaeftigungBeginn = beginn,
            beschaeftigungBeschaeftigungsart = "SOZIALVERSICHERUNGSPFLICHTIG"
        )

        val dto = entity.toDeuevAnmeldungDto()

        dto.referenzId shouldBe meldungId.toString()
        dto.mitarbeiterId shouldBe mitarbeiterId
        dto.person.vorname shouldBe "Max"
        dto.person.nachname shouldBe "Mustermann"
        dto.person.geburtsdatum shouldBe "1990-01-01"
        dto.person.sozialversicherungsnummer shouldBe "65 120490 M 123"
        dto.person.anschrift.strasse shouldBe "Hauptstraße 10"
        dto.person.anschrift.postleitzahl shouldBe "10115"
        dto.person.anschrift.ort shouldBe "Berlin"
        dto.person.kontakt?.email shouldBe "max.mustermann@beispiel.de"
        dto.person.kontakt?.telefon shouldBe "49-30-1234567"
        dto.beschaeftigung.beginn shouldBe "2025-02-01"
        dto.beschaeftigung.art shouldBe "SOZIALVERSICHERUNGSPFLICHTIG"
    }

    "NewDeuevAnmeldungenXmlDto should static mock call toDeuevAnmeldungDto" {
        val erstelltAm = LocalDateTime.of(2025, 6, 20, 10, 0)
        val export = ExportEntity(
            id = 1L,
            typ = "DEUEV_ANMELDUNG",
            status = ExportStatus.CREATED,
            betriebsnummer = "11223344",
            createdAt = erstelltAm
        )

        val entity1 = DeuevAnmeldungEntity(
            meldungId = UUID.randomUUID(),
            meldungTyp = "DEUEV_ANMELDUNG",
            meldungQuelle = "EAP",
            meldungMandantId = "M1",
            meldungErstelltAm = erstelltAm,
            arbeitgeberBetriebsnummer = "11223344",
            mitarbeiterId = "EMP-1",
            mitarbeiterVorname = "Max",
            mitarbeiterNachname = "Mustermann",
            mitarbeiterGeburtsdatum = LocalDate.of(1990, 1, 1),
            mitarbeiterSozialversicherungsnummer = "SVN1",
            mitarbeiterAnschriftStrasse = "Strasse",
            mitarbeiterAnschriftPlz = "12345",
            mitarbeiterAnschriftOrt = "Ort",
            mitarbeiterKontaktEmail = "email",
            mitarbeiterKontaktTelefonPraefix = "+49",
            mitarbeiterKontaktTelefonVorwahl = "30",
            mitarbeiterKontaktTelefonNummer = "123",
            beschaeftigungBeginn = LocalDate.of(2025, 1, 1),
            beschaeftigungBeschaeftigungsart = "ART"
        )
        val entity2 = entity1.copy(meldecenterId = UUID.randomUUID(), meldungId = UUID.randomUUID())
        val meldungen = listOf(entity1, entity2)

        mockkStatic(DeuevAnmeldungEntity::toDeuevAnmeldungDto)

        val realDto = DeuevAnmeldungDto(
            referenzId = "ref-1",
            mitarbeiterId = "EMP-1",
            person = DeuevPersonDto(
                vorname = "Max",
                nachname = "Mustermann",
                geburtsdatum = "1990-01-01",
                sozialversicherungsnummer = "SVN1",
                anschrift = DeuevAnschriftDto("Strasse", "12345", "Ort"),
                kontakt = DeuevKontaktDto("email", "phone")
            ),
            beschaeftigung = DeuevBeschaeftigungDto("2025-01-01", "ART")
        )
        every { any<DeuevAnmeldungEntity>().toDeuevAnmeldungDto() } returns realDto

        val result = newDeuevAnmeldungenXmlDto(export, meldungen)

        result.absender.betriebsnummer shouldBe "11223344"
        result.absender.erstellungszeitpunkt shouldBe erstelltAm.toString()
        result.anmeldungen.size shouldBe 2
        result.anmeldungen[0] shouldBe realDto
        result.anmeldungen[1] shouldBe realDto

        verify(exactly = 2) { any<DeuevAnmeldungEntity>().toDeuevAnmeldungDto() }

        unmockkStatic(DeuevAnmeldungEntity::toDeuevAnmeldungDto)
    }
})
