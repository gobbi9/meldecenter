package coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.EntgeltbescheinigungenAuDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgAbsenderDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgArbeitsunfaehigkeitDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgBescheinigungDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgEntgeltDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgKontaktDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgPersonDto
import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltbescheinigungAuEntity

/**
 * Mapping [EntgeltbescheinigungAuEntity] zu [KgBescheinigungDto].
 */
fun EntgeltbescheinigungAuEntity.toKgBescheinigungDto(): KgBescheinigungDto =
    KgBescheinigungDto(
        referenzId = meldungId.toString(),
        mitarbeiterId = mitarbeiterId,
        person = KgPersonDto(
            vorname = mitarbeiterVorname,
            nachname = mitarbeiterNachname,
            geburtsdatum = mitarbeiterGeburtsdatum.toString(),
            sozialversicherungsnummer = mitarbeiterSozialversicherungsnummer,
            kontakt = KgKontaktDto(
                telefon = mitarbeiterKontaktTelefonnummer.trim().replace(" ", "-"),
            )
        ),
        arbeitsunfaehigkeit = KgArbeitsunfaehigkeitDto(
            beginn = krankheitArbeitsunfaehigkeitBeginn.toString(),
            ende = krankheitArbeitsunfaehigkeitEnde.toString(),
        ),
        entgelt = KgEntgeltDto(
            bezugszeitraum = entgeltBezugszeitraum,
            bruttoentgelt = entgeltBruttoentgelt.toString()
        )
    )

/**
 * Erstellt eine neue Instanz von [EntgeltbescheinigungenAuDto] mit den angegebenen Parametern.
 */
fun newEntgeltbescheinigungenAuXmlDto(
    export: ExportEntity,
    meldungen: List<EntgeltbescheinigungAuEntity>,
): EntgeltbescheinigungenAuDto = EntgeltbescheinigungenAuDto(
    absender = KgAbsenderDto(
        betriebsnummer = export.betriebsnummer,
        erstellungszeitpunkt = export.createdAt.toString(),
    ),
    bescheinigungen = meldungen.map { it.toKgBescheinigungDto() }
)

