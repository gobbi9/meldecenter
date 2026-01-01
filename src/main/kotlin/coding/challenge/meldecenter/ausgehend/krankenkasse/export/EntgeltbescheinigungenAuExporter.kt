package coding.challenge.meldecenter.ausgehend.krankenkasse.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.Exporter
import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltbescheinigungAuRepository
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
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
): Exporter {
    @NewSpan
    @OptIn(ExperimentalCoroutinesApi::class) // flatMapMerge
    override fun export(): Flow<ExportEntity> =
        repository
            .findAllExportGroups()
            .flatMapMerge(concurrency = 2) { group ->
                flowOf(exportCreator.assign(exportGroup = group))
                    .filterNotNull()
                    .mapNotNull { exportUploader.upload(unpreparedExport = it) }
            }
}
