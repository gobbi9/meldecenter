package coding.challenge.meldecenter.ausgehend.export.event

/**
 * Typ eines Export-Events.
 */
enum class ExportEventType {
    /** Export wurde gestartet. */
    START,

    /** Export wurde beendet. */
    END,

    /** Ein Fehler ist beim Export aufgetreten. */
    ERROR,
}
