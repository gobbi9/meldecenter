package coding.challenge.meldecenter.ausgehend.krankenkasse.export

/**
 * Gruppierung von Entgeltbescheinigungen nach Betriebsnummer
 */
data class EntgeltbescheinigungAuExportGroup(
    val betriebsnummer: String,
    val count: Long,
)
