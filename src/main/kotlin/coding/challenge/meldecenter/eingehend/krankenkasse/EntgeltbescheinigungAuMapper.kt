package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.eingehend.shared.AnschriftDto
import coding.challenge.meldecenter.eingehend.shared.ArbeitgeberDto
import coding.challenge.meldecenter.eingehend.shared.MeldungDto
import coding.challenge.meldecenter.eingehend.shared.MitarbeiterDto
import coding.challenge.meldecenter.eingehend.shared.meldungen.EntgeltbescheinigungAuDto

/**
 * Mapping [EntgeltbescheinigungAuDto] zu [EntgeltbescheinigungAuEntity].
 */
fun EntgeltbescheinigungAuDto.toEntity(): EntgeltbescheinigungAuEntity =
    EntgeltbescheinigungAuEntity(
        meldungId = meldung.id,
        meldungTyp = meldung.typ,
        meldungQuelle = meldung.quelle,
        meldungMandantId = meldung.mandantId,
        meldungErstelltAm = meldung.erstelltAm,
        arbeitgeberBetriebsnummer = arbeitgeber.betriebsnummer,
        mitarbeiterId = mitarbeiter.id,
        mitarbeiterVorname = mitarbeiter.vorname,
        mitarbeiterNachname = mitarbeiter.nachname,
        mitarbeiterGEBURTSDATUM = mitarbeiter.geburtsdatum,
        mitarbeiterSozialversicherungsnummer = mitarbeiter.sozialversicherungsnummer,
        mitarbeiterAnschriftStrasse = mitarbeiter.anschrift.strasse,
        mitarbeiterAnschriftPlz = mitarbeiter.anschrift.plz,
        mitarbeiterAnschriftOrt = mitarbeiter.anschrift.ort,
        mitarbeiterKontaktEmail = mitarbeiter.kontakt.email,
        mitarbeiterKontaktTelefonnummer = mitarbeiter.kontakt.telefonnummer,
        mitarbeiterKontaktBevorzugteKontaktsart = mitarbeiter.kontakt.bevorzugteKontaktsart,
        krankheitArbeitsunfaehigkeitBeginn = krankheit.arbeitsunfaehigkeitBeginn,
        krankheitArbeitsunfaehigkeitEnde = krankheit.arbeitsunfaehigkeitEnde,
        entgeltBezugszeitraum = entgelt.bezugszeitraum,
        entgeltBruttoentgelt = entgelt.bruttoentgelt
    )

/**
 * Mapping [EntgeltbescheinigungAuEntity] zu [EntgeltbescheinigungAuDto].
 */
fun EntgeltbescheinigungAuEntity.toDto(): EntgeltbescheinigungAuDto =
    EntgeltbescheinigungAuDto(
        meldecenterId = meldecenterId,
        meldung = MeldungDto(
            id = meldungId,
            typ = meldungTyp,
            quelle = meldungQuelle,
            mandantId = meldungMandantId,
            erstelltAm = meldungErstelltAm
        ),
        arbeitgeber = ArbeitgeberDto(
            betriebsnummer = arbeitgeberBetriebsnummer
        ),
        mitarbeiter = MitarbeiterDto(
            id = mitarbeiterId,
            vorname = mitarbeiterVorname,
            nachname = mitarbeiterNachname,
            geburtsdatum = mitarbeiterGEBURTSDATUM,
            sozialversicherungsnummer = mitarbeiterSozialversicherungsnummer,
            anschrift = AnschriftDto(
                strasse = mitarbeiterAnschriftStrasse,
                plz = mitarbeiterAnschriftPlz,
                ort = mitarbeiterAnschriftOrt
            ),
            kontakt = EntgeltbescheinigungAuKontaktDto(
                email = mitarbeiterKontaktEmail,
                telefonnummer = mitarbeiterKontaktTelefonnummer,
                bevorzugteKontaktsart = mitarbeiterKontaktBevorzugteKontaktsart
            )
        ),
        krankheit = KrankheitDto(
            arbeitsunfaehigkeitBeginn = krankheitArbeitsunfaehigkeitBeginn,
            arbeitsunfaehigkeitEnde = krankheitArbeitsunfaehigkeitEnde
        ),
        entgelt = EntgeltDto(
            bezugszeitraum = entgeltBezugszeitraum,
            bruttoentgelt = entgeltBruttoentgelt
        )
    )
