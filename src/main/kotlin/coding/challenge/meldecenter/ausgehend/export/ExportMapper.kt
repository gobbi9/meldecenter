package coding.challenge.meldecenter.ausgehend.export

/**
 * Erstellt einen Dateinamen für die Entgeltbescheinigung-Au-XML-Datei basierend auf den Entity-Daten.
 * Diese Datei wird im FTP-Ordner "inbox" abgelegt.
 */
fun ExportEntity.toXmlFilename(): String {
    val today = createdAt.toLocalDate().toString().replace("-", "")
    return "${typ}_${betriebsnummer}_${today}_$id.xml"
}
