package coding.challenge.meldecenter.eingehend.eap

import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltbescheinigungAuRepository
import coding.challenge.meldecenter.eingehend.sozialversicherung.DeuevAnmeldungRepository
import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@MeldecenterSpringBootTest
class MeldungGatewayControllerIT(
    private val webTestClient: WebTestClient,
    private val deuevAnmeldungRepository: DeuevAnmeldungRepository,
    private val entgeltbescheinigungAuRepository: EntgeltbescheinigungAuRepository,
) : StringSpec({

    "POST /v1/meldung should save a new DEUEV notification" {
        val jsonResource = ClassPathResource("eingehend/rest/DEÜV-Anmeldung.json")
        val json =
            jsonResource.inputStream.bufferedReader().use { it.readText() }

        webTestClient.post()
            .uri("/v1/meldung")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(json)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.meldecenterId")
            .exists()
            .jsonPath("$.meldung.id")
            .isEqualTo("dd01fff5-778a-44ea-a87f-8ca58cad4b79")

        val saved = deuevAnmeldungRepository.findAll().toList()
        saved.any { it.meldungId.toString() == "dd01fff5-778a-44ea-a87f-8ca58cad4b79" } shouldBe true
    }

    "POST /v1/meldung should save a new incapacity to work certificate notification" {
        val jsonResource =
            ClassPathResource("eingehend/rest/Entgeltbescheinigung-Arbeitsunfähigkeit.json")
        val json =
            jsonResource.inputStream.bufferedReader().use { it.readText() }

        webTestClient.post()
            .uri("/v1/meldung")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(json)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.meldecenterId")
            .exists()
            .jsonPath("$.meldung.id")
            .isEqualTo("b8a85b87-1b5f-4f5d-abf2-c6c7969a4609")

        val saved = entgeltbescheinigungAuRepository.findAll().toList()
        saved.any { it.meldungId.toString() == "b8a85b87-1b5f-4f5d-abf2-c6c7969a4609" } shouldBe true
    }
})
