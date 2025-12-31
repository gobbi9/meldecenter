package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.eingehend.shared.AnschriftDto
import coding.challenge.meldecenter.eingehend.shared.ArbeitgeberDto
import coding.challenge.meldecenter.eingehend.shared.MeldungDto
import coding.challenge.meldecenter.eingehend.shared.MitarbeiterDto
import coding.challenge.meldecenter.eingehend.shared.meldungen.EntgeltbescheinigungAuDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalDate
import java.util.UUID

/**
 * Test für [EntgeltbescheinigungAuDto.toEntity()] und [EntgeltbescheinigungAuEntity.toDto()].
 */
class EntgeltbescheinigungAuMapperTest : StringSpec({

    val meldecenterId = UUID.randomUUID()
    val meldungId = UUID.randomUUID()
    val erstelltAm = LocalDateTime.parse("2025-03-10T11:05:00")
    val geburtsdatum = LocalDate.of(1985, 9, 3)
    val auBeginn = LocalDate.of(2025, 3, 1)
    val auEnde = LocalDate.of(2025, 3, 20)

    val dto = EntgeltbescheinigungAuDto(
        meldecenterId = meldecenterId,
        meldung = MeldungDto(
            id = meldungId,
            typ = "ENTGELTBESCHEINIGUNG_KG",
            quelle = "EAP_DATEV",
            mandantId = "MANDANT_4711",
            erstelltAm = erstelltAm
        ),
        arbeitgeber = ArbeitgeberDto(
            betriebsnummer = "12345678"
        ),
        mitarbeiter = MitarbeiterDto(
            id = "EMP-4452",
            vorname = "Anna",
            nachname = "Schmidt",
            geburtsdatum = geburtsdatum,
            sozialversicherungsnummer = "12 030985 S 456",
            anschrift = AnschriftDto(
                strasse = "Marktweg 5",
                plz = "80331",
                ort = "München"
            ),
            kontakt = EntgeltbescheinigungAuKontaktDto(
                email = "anna.schmidt@bespiel.de",
                telefonnummer = "+49-89-987654",
                bevorzugteKontaktsart = "TELEFON"
            )
        ),
        krankheit = KrankheitDto(
            arbeitsunfaehigkeitBeginn = auBeginn,
            arbeitsunfaehigkeitEnde = auEnde
        ),
        entgelt = EntgeltDto(
            bezugszeitraum = "2024-12",
            bruttoentgelt = BigDecimal("3200.00")
        )
    )

    val entity = EntgeltbescheinigungAuEntity(
        meldecenterId = meldecenterId,
        meldungId = meldungId,
        meldungTyp = "ENTGELTBESCHEINIGUNG_KG",
        meldungQuelle = "EAP_DATEV",
        meldungMandantId = "MANDANT_4711",
        meldungErstelltAm = erstelltAm,
        arbeitgeberBetriebsnummer = "12345678",
        mitarbeiterId = "EMP-4452",
        mitarbeiterVorname = "Anna",
        mitarbeiterNachname = "Schmidt",
        mitarbeiterGeburtsdatum = geburtsdatum,
        mitarbeiterSozialversicherungsnummer = "12 030985 S 456",
        mitarbeiterAnschriftStrasse = "Marktweg 5",
        mitarbeiterAnschriftPlz = "80331",
        mitarbeiterAnschriftOrt = "München",
        mitarbeiterKontaktEmail = "anna.schmidt@bespiel.de",
        mitarbeiterKontaktTelefonnummer = "+49-89-987654",
        mitarbeiterKontaktBevorzugteKontaktsart = "TELEFON",
        krankheitArbeitsunfaehigkeitBeginn = auBeginn,
        krankheitArbeitsunfaehigkeitEnde = auEnde,
        entgeltBezugszeitraum = "2024-12",
        entgeltBruttoentgelt = BigDecimal("3200.00")
    )

    "Map EntgeltbescheinigungAuDto to EntgeltbescheinigungAuEntity" {
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
        result.mitarbeiterKontaktTelefonnummer shouldBe dto.mitarbeiter.kontakt.telefonnummer
        result.mitarbeiterKontaktBevorzugteKontaktsart shouldBe dto.mitarbeiter.kontakt.bevorzugteKontaktsart
        result.krankheitArbeitsunfaehigkeitBeginn shouldBe dto.krankheit.arbeitsunfaehigkeitBeginn
        result.krankheitArbeitsunfaehigkeitEnde shouldBe dto.krankheit.arbeitsunfaehigkeitEnde
        result.entgeltBezugszeitraum shouldBe dto.entgelt.bezugszeitraum
        result.entgeltBruttoentgelt shouldBe dto.entgelt.bruttoentgelt
    }

    "Map EntgeltbescheinigungAuEntity to EntgeltbescheinigungAuDto" {
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
        result.mitarbeiter.kontakt.telefonnummer shouldBe entity.mitarbeiterKontaktTelefonnummer
        result.mitarbeiter.kontakt.bevorzugteKontaktsart shouldBe entity.mitarbeiterKontaktBevorzugteKontaktsart
        result.krankheit.arbeitsunfaehigkeitBeginn shouldBe entity.krankheitArbeitsunfaehigkeitBeginn
        result.krankheit.arbeitsunfaehigkeitEnde shouldBe entity.krankheitArbeitsunfaehigkeitEnde
        result.entgelt.bezugszeitraum shouldBe entity.entgeltBezugszeitraum
        result.entgelt.bruttoentgelt shouldBe entity.entgeltBruttoentgelt
    }
})
