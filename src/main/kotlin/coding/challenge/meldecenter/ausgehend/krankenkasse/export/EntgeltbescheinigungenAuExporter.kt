package coding.challenge.meldecenter.ausgehend.krankenkasse.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.event.ExportEventService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 * Gruppiert Entgeltbescheinigungen Arbeitsunfähigkeit und erstellt ein [ExportEntity]
 */
@Service
class EntgeltbescheinigungenAuExporter(
    private val exportAssigner: EntgeltbescheinigungenAuExportAssigner,
    private val exportCreator: EntgeltbescheinigungenAuExportCreator,
    private val exportPreparer: EntgeltbescheinigungenAuExportPreparer,
    private val exportUploader: EntgeltbescheinigungenAuExportUploader,
    private val exportEventService: ExportEventService,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    @NewSpan
    fun export(): Flow<ExportEntity> =
        exportAssigner
            .findAllExportGroups()
            .onEach { log.info { "EntgeltbescheinigungenAu Export Gruppe: $it" } }
            .flatMapMerge(concurrency = 2) { group ->
                flowOf(exportCreator.assign(exportGroup = group))
                    .filterNotNull()
                    .onEach { exportEventService.saveAssignedEvent(exportId = it.id) }
                    .mapNotNull { exportPreparer.prepare(export = it) }
                    .onEach { exportEventService.saveStartEvent(exportId = it.id) }
                    .mapNotNull { exportUploader.upload(export = it) }
                    .onEach { exportEventService.saveEndOrErrorEvent(export = it) }
            }
}
