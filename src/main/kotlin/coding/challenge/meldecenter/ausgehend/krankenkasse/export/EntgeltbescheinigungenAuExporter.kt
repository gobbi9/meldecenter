package coding.challenge.meldecenter.ausgehend.krankenkasse.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.Exporter
import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltbescheinigungAuRepository
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import org.springframework.stereotype.Service

/**
 * Gruppiert Entgeltbescheinigungen Arbeitsunfähigkeit und erstellt ein [ExportEntity]
 */
@Service
class EntgeltbescheinigungenAuExporter(
    private val repository: EntgeltbescheinigungAuRepository,
    private val exportCreator: EntgeltbescheinigungenAuExportCreator,
    private val exportUploader: EntgeltbescheinigungenAuExportUploader,
) : Exporter {
    @NewSpan
    override fun export(): Flow<ExportEntity> =
        repository
            .findAllExportGroups()
            .mapNotNull { exportCreator.assign(exportGroup = it) }
            .mapNotNull { exportUploader.upload(unpreparedExport = it) }
}
