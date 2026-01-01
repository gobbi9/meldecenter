package coding.challenge.meldecenter.ausgehend.sozialversicherung.export

/**
 * Gruppierung von DEÜV-Anmeldungen nach Betriebsnummer.
 *
 * @property betriebsnummer Die Betriebsnummer des Arbeitgebers.
 * @property count Die Anzahl der Meldungen in dieser Gruppe.
 */
data class DeuevAnmeldungExportGroup(
    val betriebsnummer: String,
    val count: Long,
)
