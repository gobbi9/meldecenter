package coding.challenge.meldecenter.eingehend.sozialversicherung

import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

private val log = KotlinLogging.logger {}

/**
 * Controller für DEÜV-Anmeldungen.
 * Stellt Endpunkte für die Verarbeitung von Anmeldungen bereit.
 */
@RestController
@RequestMapping("/v1/sozialversicherung/deuev-anmeldung")
@Tag(name = "DEÜV-Anmeldung", description = "Endpunkte für DEÜV-Anmeldungen")
class DeuvAnmeldungController(
    private val service: DeuvAnmeldungService
) {

    /**
     * Empfängt und speichert eine DEÜV-Anmeldung.
     * @param dto Die Anmeldung im JSON-Format.
     * @return Die gespeicherte Anmeldung.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "DEÜV-Anmeldung empfangen", description = "Empfängt eine DEÜV-Anmeldung und speichert diese.")
    suspend fun postDeuevAnmeldung(@RequestBody dto: DeuvAnmeldungDto): DeuevAnmeldungEntity {
        log.debug { "POST /v1/sozialversicherung/deuev-anmeldung aufgerufen" }
        log.trace { "Request Body: $dto" }

        return service.save(dto)
    }
}
