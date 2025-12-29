package coding.challenge.meldecenter.ausgehend.export

/**
 * Status eines Exports.
 */
enum class ExportStatus {
    /** Export wurde erstellt. */
    CREATED,

    /** Export wird gerade verarbeitet. */
    EXPORTING,

    /** Export wurde erfolgreich abgeschlossen. */
    EXPORTED,

    /** Export ist fehlgeschlagen. */
    FAILED,

    /** Export ist ein Duplikat. */
    DUPLICATES,
}
