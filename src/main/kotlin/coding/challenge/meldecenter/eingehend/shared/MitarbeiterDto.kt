package coding.challenge.meldecenter.eingehend.shared

import java.time.LocalDate

/**
 * Daten des Mitarbeiters.
 * Beispiel: MitarbeiterDto(vorname = "Max", nachname = "Mustermann", ...)
 */
data class MitarbeiterDto(
    val id: String,
    val vorname: String,
    val nachname: String,
    val geburtsdatum: LocalDate,
    val sozialversicherungsnummer: String,
    val anschrift: AnschriftDto,
    val kontakt: KontaktDto
)
