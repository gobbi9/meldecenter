package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.eingehend.shared.AnschriftDto
import coding.challenge.meldecenter.eingehend.shared.ArbeitgeberDto
import coding.challenge.meldecenter.eingehend.shared.MeldungDto
import coding.challenge.meldecenter.eingehend.shared.MitarbeiterDto
import coding.challenge.meldecenter.eingehend.shared.TelefonDto
import coding.challenge.meldecenter.eingehend.shared.meldungen.DeuevAnmeldungDto
import coding.challenge.meldecenter.shared.sozialversicherung.DeuevAnmeldungEntity

/**
 * Mapping [DeuevAnmeldungDto] zu [DeuevAnmeldungEntity].
 */
fun DeuevAnmeldungDto.toEntity(): DeuevAnmeldungEntity =
    DeuevAnmeldungEntity(
        meldungId = meldung.id,
        meldungTyp = meldung.typ,
        meldungQuelle = meldung.quelle,
        meldungMandantId = meldung.mandantId,
        meldungErstelltAm = meldung.erstelltAm,
        arbeitgeberBetriebsnummer = arbeitgeber.betriebsnummer,
        mitarbeiterId = mitarbeiter.id,
        mitarbeiterVorname = mitarbeiter.vorname,
        mitarbeiterNachname = mitarbeiter.nachname,
        mitarbeiterGeburtsdatum = mitarbeiter.geburtsdatum,
        mitarbeiterSozialversicherungsnummer = mitarbeiter.sozialversicherungsnummer,
        mitarbeiterAnschriftStrasse = mitarbeiter.anschrift.strasse,
        mitarbeiterAnschriftPlz = mitarbeiter.anschrift.plz,
        mitarbeiterAnschriftOrt = mitarbeiter.anschrift.ort,
        mitarbeiterKontaktEmail = mitarbeiter.kontakt.email,
        mitarbeiterKontaktTelefonPraefix = mitarbeiter.kontakt.telefon.praefix,
        mitarbeiterKontaktTelefonVorwahl = mitarbeiter.kontakt.telefon.vorwahl,
        mitarbeiterKontaktTelefonNummer = mitarbeiter.kontakt.telefon.nummer,
        beschaeftigungBeginn = beschaeftigung.beginn,
        beschaeftigungBeschaeftigungsart = beschaeftigung.beschaeftigungsart
    )

/**
 * Mapping [DeuevAnmeldungEntity] zu [DeuevAnmeldungDto].
 */
fun DeuevAnmeldungEntity.toDto(): DeuevAnmeldungDto =
    DeuevAnmeldungDto(
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
            geburtsdatum = mitarbeiterGeburtsdatum,
            sozialversicherungsnummer = mitarbeiterSozialversicherungsnummer,
            anschrift = AnschriftDto(
                strasse = mitarbeiterAnschriftStrasse,
                plz = mitarbeiterAnschriftPlz,
                ort = mitarbeiterAnschriftOrt
            ),
            kontakt = DeuevAnmeldungKontaktDto(
                email = mitarbeiterKontaktEmail,
                telefon = TelefonDto(
                    praefix = mitarbeiterKontaktTelefonPraefix,
                    vorwahl = mitarbeiterKontaktTelefonVorwahl,
                    nummer = mitarbeiterKontaktTelefonNummer
                )
            )
        ),
        beschaeftigung = BeschaeftigungDto(
            beginn = beschaeftigungBeginn,
            beschaeftigungsart = beschaeftigungBeschaeftigungsart
        )
    )
