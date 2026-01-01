package coding.challenge.meldecenter.ausgehend.export.event

/**
 * Erstellt eine neue [ExportEventEntity].
 *
 * @param exportId Die ID des Exports, zu dem dieses Event gehört.
 * @param type Der Typ des Events (z. B. START, END, ERROR).
 * @param details Zusätzliche Details oder Fehlermeldungen (maximal 1000 Zeichen).
 * @param traceId Die Trace-ID zur Nachverfolgung über Systemgrenzen hinweg.
 * @return Eine vorkonfigurierte Export-Event-Entität.
 */
fun newExportEvent(
    exportId: Long,
    type: ExportEventType,
    details: String? = null,
    traceId: String,
): ExportEventEntity = ExportEventEntity(
    exportId = exportId,
    type = type,
    details = details?.take(1000),
    traceId = traceId,
    createdBy = "SYSTEM"
)
