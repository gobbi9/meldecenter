package coding.challenge.meldecenter.ausgehend.export

/**
 * Erstellt einen Dateinamen für die Export-Datei (z. B. XML) basierend auf den Daten der [ExportEntity].
 *
 * Der Dateiname setzt sich aus dem Typ, der Betriebsnummer, dem Datum und der ID zusammen.
 * Beispiel: `TYP_12345678_20240101_1.xml`
 *
 * @return Der generierte Dateiname als String.
 */
fun ExportEntity.toXmlFilename(): String {
    val today = createdAt.toLocalDate().toString().replace("-", "")
    return "${typ}_${betriebsnummer}_${today}_$id.xml"
}
