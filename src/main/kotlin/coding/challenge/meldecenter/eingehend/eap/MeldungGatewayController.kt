package coding.challenge.meldecenter.eingehend.eap

import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltbescheinigungAuController
import coding.challenge.meldecenter.eingehend.shared.meldungen.DeuevAnmeldungDto
import coding.challenge.meldecenter.eingehend.shared.meldungen.EntgeltbescheinigungAuDto
import coding.challenge.meldecenter.eingehend.shared.meldungen.Meldung
import coding.challenge.meldecenter.eingehend.sozialversicherung.DeuevAnmeldungController
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

private val log = KotlinLogging.logger {}

/**
 * Gateway Controller für alle Meldungstypen.
 */
@RestController
@RequestMapping("/v1/meldung")
@Tag(
    name = "Meldung Gateway",
    description = "Gateway Controller für alle Meldungstypen"
)
class MeldungGatewayController(
    private val deuevAnmeldungController: DeuevAnmeldungController,
    private val entgeltbescheinigungAuController: EntgeltbescheinigungAuController,
) {

    /**
     * Empfängt und speichert eine Meldung.
     * @param meldung Die Meldung im JSON-Format.
     * @return Die gespeicherte Anmeldung.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Meldung empfangen",
        description = "Leitet eine Meldung weiter."
    )
    @NewSpan
    suspend fun save(@RequestBody meldung: Meldung): Meldung {
        log.debug { "POST /v1/meldung aufgerufen" }
        log.trace { "Request Body: $meldung" }

        return when (meldung) {
            is DeuevAnmeldungDto ->
                deuevAnmeldungController.save(meldung)
            is EntgeltbescheinigungAuDto ->
                entgeltbescheinigungAuController.save(meldung)
        }
    }
}
