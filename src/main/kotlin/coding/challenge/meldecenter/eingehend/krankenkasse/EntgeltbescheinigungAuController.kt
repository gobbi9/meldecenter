package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.eingehend.shared.meldungen.EntgeltbescheinigungAuDto
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

/**
 * Controller für Entgeltbescheinigungen bei Arbeitsunfähigkeit.
 */
@RestController
@RequestMapping("/v1/krankenkasse/entgeltbescheinigung-au")
@Tag(name = "Entgeltbescheinigung AU", description = "Endpunkte für Entgeltbescheinigungen bei Arbeitsunfähigkeit")
class EntgeltbescheinigungAuController(
    private val entgeltbescheinigungAuService: EntgeltbescheinigungAuService,
) {

    /**
     * Empfängt und speichert eine Entgeltbescheinigung.
     * @param entgeltbescheinigungAuDto Die Bescheinigung im JSON-Format.
     * @return Die gespeicherte Bescheinigung.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Entgeltbescheinigung AU empfangen",
        description = "Empfängt eine Entgeltbescheinigung AU und speichert diese."
    )
    @NewSpan
    suspend fun save(
        @RequestBody entgeltbescheinigungAuDto: EntgeltbescheinigungAuDto,
    ): EntgeltbescheinigungAuDto {
        log.debug { "POST /v1/krankenkasse/entgeltbescheinigung-au aufgerufen" }
        log.trace { "Request Body: $entgeltbescheinigungAuDto" }

        return entgeltbescheinigungAuService.save(entgeltbescheinigungAuDto)
    }

    /**
     * Liest Entgeltbescheinigungen paginiert aus.
     * @param pageable Die Pagination-Informationen.
     * @return Ein Flow von Entgeltbescheinigungen.
     */
    @GetMapping
    @Operation(
        summary = "Entgeltbescheinigungen AU lesen",
        description = "Liest alle Entgeltbescheinigungen AU paginiert aus."
    )
    @NewSpan
    fun getPage(
        @PageableDefault(
            size = 10,
            sort = ["meldecenterId"],
            direction = Sort.Direction.ASC
        ) pageable: Pageable,
    ): Flow<EntgeltbescheinigungAuDto> {
        log.debug { "GET /v1/krankenkasse/entgeltbescheinigung-au aufgerufen mit pageable=$pageable" }
        return entgeltbescheinigungAuService.findAll(pageable)
    }
}
