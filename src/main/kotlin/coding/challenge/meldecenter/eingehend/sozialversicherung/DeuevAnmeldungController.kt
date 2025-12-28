package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.eingehend.shared.meldungen.DeuevAnmeldungDto
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
 * Controller für DEÜV-Anmeldungen.
 * Stellt Endpunkte für die Verarbeitung von Anmeldungen bereit.
 */
@RestController
@RequestMapping("/v1/sozialversicherung/deuev-anmeldung")
@Tag(name = "DEÜV-Anmeldung", description = "Endpunkte für DEÜV-Anmeldungen")
class DeuevAnmeldungController(
    private val deuevAnmeldungService: DeuevAnmeldungService,
) {

    /**
     * Empfängt und speichert eine DEÜV-Anmeldung.
     * @param deuevAnmeldungDto Die Anmeldung im JSON-Format.
     * @return Die gespeicherte Anmeldung.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "DEÜV-Anmeldung empfangen",
        description = "Empfängt eine DEÜV-Anmeldung und speichert diese."
    )
    @NewSpan
    suspend fun postDeuevAnmeldung(
        @RequestBody deuevAnmeldungDto: DeuevAnmeldungDto,
    ): DeuevAnmeldungDto {
        log.debug { "POST /v1/sozialversicherung/deuev-anmeldung aufgerufen" }
        log.trace { "Request Body: $deuevAnmeldungDto" }

        return deuevAnmeldungService.save(deuevAnmeldungDto)
    }

    /**
     * Liest DEÜV-Anmeldungen paginiert aus.
     * @param page Die Seitenzahl (0-basiert).
     * @param size Die Anzahl der Elemente pro Seite.
     * @param sort Die Sortierung.
     * @return Ein Flow von DEÜV-Anmeldungen.
     */
    @GetMapping
    @Operation(
        summary = "DEÜV-Anmeldungen lesen",
        description = "Liest alle DEÜV-Anmeldungen paginiert aus."
    )
    @NewSpan
    fun getDeuevAnmeldungen(
        @PageableDefault(
            size = 10,
            sort = ["meldecenterId"],
            direction = Sort.Direction.ASC
        ) pageable: Pageable,
    ): Flow<DeuevAnmeldungDto> {
        log.debug { "GET /v1/sozialversicherung/deuev-anmeldung aufgerufen mit pageable=$pageable" }
        return deuevAnmeldungService.findAll(pageable)
    }
}
