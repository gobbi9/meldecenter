package coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevAbsenderDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevAnmeldungDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevAnmeldungenDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevAnschriftDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevBeschaeftigungDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevKontaktDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevPersonDto
import coding.challenge.meldecenter.shared.sozialversicherung.DeuevAnmeldungEntity

/**
 * Mapping [DeuevAnmeldungEntity] zu [DeuevAnmeldungDto].
 */
fun DeuevAnmeldungEntity.toDeuevAnmeldungDto(): DeuevAnmeldungDto =
    DeuevAnmeldungDto(
        referenzId = meldungId.toString(),
        mitarbeiterId = mitarbeiterId,
        person = DeuevPersonDto(
            vorname = mitarbeiterVorname,
            nachname = mitarbeiterNachname,
            geburtsdatum = mitarbeiterGeburtsdatum.toString(),
            sozialversicherungsnummer = mitarbeiterSozialversicherungsnummer,
            anschrift = DeuevAnschriftDto(
                strasse = mitarbeiterAnschriftStrasse,
                postleitzahl = mitarbeiterAnschriftPlz,
                ort = mitarbeiterAnschriftOrt
            ),
            kontakt = DeuevKontaktDto(
                email = mitarbeiterKontaktEmail,
                telefon = "${mitarbeiterKontaktTelefonPraefix}-${mitarbeiterKontaktTelefonVorwahl}-${mitarbeiterKontaktTelefonNummer}".trim()
                    .replace("+", ""),
            )
        ),
        beschaeftigung = DeuevBeschaeftigungDto(
            beginn = beschaeftigungBeginn.toString(),
            art = beschaeftigungBeschaeftigungsart
        )
    )

/**
 * Erstellt eine neue Instanz von [DeuevAnmeldungenDto] mit den angegebenen Parametern.
 */
fun newDeuevAnmeldungenXmlDto(
    export: ExportEntity,
    meldungen: List<DeuevAnmeldungEntity>,
): DeuevAnmeldungenDto = DeuevAnmeldungenDto(
    absender = DeuevAbsenderDto(
        betriebsnummer = export.betriebsnummer,
        erstellungszeitpunkt = export.createdAt.toString(),
    ),
    anmeldungen = meldungen.map { it.toDeuevAnmeldungDto() }
)
