package coding.challenge.meldecenter.ausgehend.export

import coding.challenge.meldecenter.eingehend.shared.MeldungTyp

/**
 * Erstellt eine neue [ExportEntity] mit dem Status [ExportStatus.CREATED].
 *
 * @param typ Der Typ der zu exportierenden Meldung.
 * @param betriebsnummer Die Betriebsnummer des Arbeitgebers.
 * @return Eine vorkonfigurierte Export-Entität.
 */
fun newExport(
    typ: MeldungTyp,
    betriebsnummer: String,
): ExportEntity = ExportEntity(
    typ = typ.toString(),
    status = ExportStatus.CREATED,
    betriebsnummer = betriebsnummer,
)
