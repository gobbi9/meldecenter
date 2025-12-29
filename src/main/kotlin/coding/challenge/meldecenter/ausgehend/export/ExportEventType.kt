package coding.challenge.meldecenter.ausgehend.export

/**
 * Typ eines Export-Events.
 */
enum class ExportEventType {
    /** Export wurde gestartet. */
    START_EXPORT,

    /** Export wurde beendet. */
    END_EXPORT,

    /** Ein Fehler ist beim Export aufgetreten. */
    ERROR,
}
