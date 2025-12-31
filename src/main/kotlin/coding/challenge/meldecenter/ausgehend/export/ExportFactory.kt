package coding.challenge.meldecenter.ausgehend.export

import coding.challenge.meldecenter.eingehend.shared.MeldungTyp

/** Erstellt einen neuen Export */
fun newExport(
    typ: MeldungTyp,
    betriebsnummer: String,
): ExportEntity = ExportEntity(
    typ = typ.toString(),
    status = ExportStatus.CREATED,
    betriebsnummer = betriebsnummer,
)
