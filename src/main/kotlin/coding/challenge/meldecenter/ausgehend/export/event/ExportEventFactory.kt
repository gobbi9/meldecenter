package coding.challenge.meldecenter.ausgehend.export.event

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
