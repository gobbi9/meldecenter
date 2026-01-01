package coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportStatus
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgArbeitsunfaehigkeitDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgBescheinigungDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgEntgeltDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgKontaktDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgPersonDto
import coding.challenge.meldecenter.shared.krankenkasse.EntgeltbescheinigungAuEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class EntgeltbescheinigungAuMapperTest : StringSpec({

    "ToKgBescheinigungDto should map entity to DTO" {
        val meldungId = UUID.randomUUID()
        val mitarbeiterId = "EMP-123"
        val geburtsdatum = LocalDate.of(1990, 1, 1)
        val auBeginn = LocalDate.of(2025, 1, 1)
        val auEnde = LocalDate.of(2025, 1, 10)

        val entity = EntgeltbescheinigungAuEntity(
            meldungId = meldungId,
            meldungTyp = "ENTGELTBESCHEINIGUNG_KG",
            meldungQuelle = "EAP_DATEV",
            meldungMandantId = "MANDANT_1",
            meldungErstelltAm = LocalDateTime.now(),
            arbeitgeberBetriebsnummer = "12345678",
            mitarbeiterId = mitarbeiterId,
            mitarbeiterVorname = "Max",
            mitarbeiterNachname = "Mustermann",
            mitarbeiterGeburtsdatum = geburtsdatum,
            mitarbeiterSozialversicherungsnummer = "12345678M001",
            mitarbeiterAnschriftStrasse = "Hauptstr. 1",
            mitarbeiterAnschriftPlz = "12345",
            mitarbeiterAnschriftOrt = "Berlin",
            mitarbeiterKontaktEmail = "max@example.com",
            mitarbeiterKontaktTelefonnummer = " +49 30 12 34567 ",
            mitarbeiterKontaktBevorzugteKontaktsart = "EMAIL",
            krankheitArbeitsunfaehigkeitBeginn = auBeginn,
            krankheitArbeitsunfaehigkeitEnde = auEnde,
            entgeltBezugszeitraum = "2025-01",
            entgeltBruttoentgelt = BigDecimal("4000.00")
        )

        val dto = entity.toKgBescheinigungDto()

        dto.referenzId shouldBe meldungId.toString()
        dto.mitarbeiterId shouldBe mitarbeiterId
        dto.person.vorname shouldBe "Max"
        dto.person.nachname shouldBe "Mustermann"
        dto.person.geburtsdatum shouldBe "1990-01-01"
        dto.person.sozialversicherungsnummer shouldBe "12345678M001"
        dto.person.kontakt.telefon shouldBe "49-30-12-34567"
        dto.arbeitsunfaehigkeit.beginn shouldBe "2025-01-01"
        dto.arbeitsunfaehigkeit.ende shouldBe "2025-01-10"
        dto.entgelt.bezugszeitraum shouldBe "2025-01"
        dto.entgelt.bruttoentgelt shouldBe "4000.00"
    }

    "NewEntgeltbescheinigungenAuXmlDto should static mock call toKgBescheinigungDto" {
        val erstelltAm = LocalDateTime.of(2025, 3, 10, 11, 5)
        val export = ExportEntity(
            id = 1L,
            typ = "KG_ENTG",
            status = ExportStatus.CREATED,
            betriebsnummer = "87654321",
            createdAt = erstelltAm
        )
        val entity1 = EntgeltbescheinigungAuEntity(
            meldungId = UUID.randomUUID(),
            meldungTyp = "ENTGELTBESCHEINIGUNG_KG",
            meldungQuelle = "EAP_DATEV",
            meldungMandantId = "MANDANT_1",
            meldungErstelltAm = erstelltAm,
            arbeitgeberBetriebsnummer = "87654321",
            mitarbeiterId = "EMP-1",
            mitarbeiterVorname = "Max",
            mitarbeiterNachname = "Mustermann",
            mitarbeiterGeburtsdatum = LocalDate.of(1990, 1, 1),
            mitarbeiterSozialversicherungsnummer = "SVN1",
            mitarbeiterAnschriftStrasse = "Strasse",
            mitarbeiterAnschriftPlz = "12345",
            mitarbeiterAnschriftOrt = "Berlin",
            mitarbeiterKontaktEmail = "email",
            mitarbeiterKontaktTelefonnummer = "123",
            mitarbeiterKontaktBevorzugteKontaktsart = "EMAIL",
            krankheitArbeitsunfaehigkeitBeginn = LocalDate.of(2025, 1, 1),
            krankheitArbeitsunfaehigkeitEnde = LocalDate.of(2025, 1, 10),
            entgeltBezugszeitraum = "2025-01",
            entgeltBruttoentgelt = BigDecimal("4000.00")
        )
        val entity2 = entity1.copy(meldecenterId = UUID.randomUUID(), meldungId = UUID.randomUUID())
        val meldungen = listOf(entity1, entity2)

        mockkStatic(EntgeltbescheinigungAuEntity::toKgBescheinigungDto)

        val realDto = KgBescheinigungDto(
            referenzId = "ref-1",
            mitarbeiterId = "EMP-1",
            person = KgPersonDto(
                vorname = "Max",
                nachname = "Mustermann",
                geburtsdatum = "1990-01-01",
                sozialversicherungsnummer = "SVN1",
                kontakt = KgKontaktDto(telefon = "123")
            ),
            arbeitsunfaehigkeit = KgArbeitsunfaehigkeitDto("2025-01-01", "2025-01-10"),
            entgelt = KgEntgeltDto("2025-01", "4000.00")
        )
        every { any<EntgeltbescheinigungAuEntity>().toKgBescheinigungDto() } returns realDto

        val result = newEntgeltbescheinigungenAuXmlDto(export, meldungen)

        result.absender.betriebsnummer shouldBe "87654321"
        result.absender.erstellungszeitpunkt shouldBe erstelltAm.toString()
        result.bescheinigungen.size shouldBe 2
        result.bescheinigungen[0] shouldBe realDto
        result.bescheinigungen[1] shouldBe realDto

        verify(exactly = 2) { any<EntgeltbescheinigungAuEntity>().toKgBescheinigungDto() }

        unmockkStatic(EntgeltbescheinigungAuEntity::toKgBescheinigungDto)
    }
})
