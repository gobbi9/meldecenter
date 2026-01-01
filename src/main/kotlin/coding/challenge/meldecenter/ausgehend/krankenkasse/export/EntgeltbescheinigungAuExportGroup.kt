package coding.challenge.meldecenter.ausgehend.krankenkasse.export

/**
 * Gruppierung von Entgeltbescheinigungen nach Betriebsnummer.
 *
 * @property betriebsnummer Die Betriebsnummer des Arbeitgebers.
 * @property count Die Anzahl der Meldungen in dieser Gruppe.
 */
data class EntgeltbescheinigungAuExportGroup(
    val betriebsnummer: String,
    val count: Long,
)
