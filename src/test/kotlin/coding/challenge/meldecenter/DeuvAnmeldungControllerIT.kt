package coding.challenge.meldecenter

import coding.challenge.meldecenter.eingehend.sozialversicherung.DeuevAnmeldungRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.toList
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@MeldecenterSpringBootTest
class DeuvAnmeldungControllerIT(
    private val webTestClient: WebTestClient,
    private val repository: DeuevAnmeldungRepository
) : StringSpec({

    "POST /v1/sozialversicherung/deuev-anmeldung should save a new registration" {
        val jsonResource = ClassPathResource("eingehend/DEÜV-Anmeldung.json")
        val json = jsonResource.inputStream.bufferedReader().use { it.readText() }

        webTestClient.post()
            .uri("/v1/sozialversicherung/deuev-anmeldung")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(json)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.meldecenterId").exists()
            .jsonPath("$.meldungId").isEqualTo("dd01fff5-778a-44ea-a87f-8ca58cad4b79")

        val saved = repository.findAll().toList()
        saved.any { it.meldungId.toString() == "dd01fff5-778a-44ea-a87f-8ca58cad4b79" } shouldBe true
    }
})
