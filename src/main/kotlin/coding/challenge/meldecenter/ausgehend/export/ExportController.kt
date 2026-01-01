package coding.challenge.meldecenter.ausgehend.export

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

@RestController
@RequestMapping("/v1/meldung")
@Tag(
    name = "Meldung Exporter",
    description = "Exporter Controller für alle Meldungstypen"
)
class ExportController(
    private val exporters: List<Exporter>,
) {
    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    @OptIn(ExperimentalCoroutinesApi::class) // flatMapConcat
    @PostMapping("/export")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
        summary = "Exportiert alle Meldungen",
        description = "Exportiert alle Meldungen im Hintergrund"
    )
    @NewSpan
    suspend fun export(): ResponseEntity<Unit> {
        log.debug { "POST /v1/meldung/export aufgerufen" }

        val context = currentCoroutineContext() // propagate traceId
        applicationScope.launch(context.minusKey(Job) + Dispatchers.Default) {
            exporters.asFlow()
                .flatMapConcat { it.export() }
                .flowOn(Dispatchers.IO)
                .onCompletion { log.debug { "Export abgeschlossen" } }
                .toList()
                .groupBy { it.typ }
                .forEach { (exportType, entities) ->
                    log.info { "[${entities.size}] Exports abgeschlossen für Typ: $exportType" }
                }
        }

        return ResponseEntity.accepted().build()
    }
}
