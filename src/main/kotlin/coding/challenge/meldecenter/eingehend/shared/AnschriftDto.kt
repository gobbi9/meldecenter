package coding.challenge.meldecenter.eingehend.shared

/**
 * Anschrift des Mitarbeiters.
 * Beispiel: AnschriftDto(strasse = "Hauptstraße 10", plz = "10115", ort = "Berlin")
 */
data class AnschriftDto(
    val strasse: String,
    val plz: String,
    val ort: String
)
