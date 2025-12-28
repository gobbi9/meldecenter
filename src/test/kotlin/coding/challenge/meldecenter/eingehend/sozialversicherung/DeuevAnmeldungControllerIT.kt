package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@MeldecenterSpringBootTest
class DeuevAnmeldungControllerIT(
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
            .jsonPath("$.meldung.id").isEqualTo("dd01fff5-778a-44ea-a87f-8ca58cad4b79")

        val saved = repository.findAll().toList()
        saved.any { it.meldungId.toString() == "dd01fff5-778a-44ea-a87f-8ca58cad4b79" } shouldBe true
    }

    "GET /v1/sozialversicherung/deuev-anmeldung should return paginated results" {
        // Given there are at least 2 entries from test data migration

        // When requesting first page with size 1
        webTestClient.get()
            .uri("/v1/sozialversicherung/deuev-anmeldung?page=0&size=1&sort=meldecenterId,asc")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.length()").isEqualTo(1)
            .jsonPath("$[0].meldecenterId").isEqualTo("00000000-0000-0000-0000-000000000001")

        // When requesting second page with size 1
        webTestClient.get()
            .uri("/v1/sozialversicherung/deuev-anmeldung?page=1&size=1&sort=meldecenterId,asc")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.length()").isEqualTo(1)
            .jsonPath("$[0].meldecenterId").isEqualTo("00000000-0000-0000-0000-000000000002")
    }
})
