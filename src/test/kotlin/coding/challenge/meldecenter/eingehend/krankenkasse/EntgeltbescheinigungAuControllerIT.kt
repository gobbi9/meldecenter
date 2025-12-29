package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@MeldecenterSpringBootTest
class EntgeltbescheinigungAuControllerIT(
    private val webTestClient: WebTestClient,
    private val repository: EntgeltbescheinigungAuRepository
) : StringSpec({

    "POST /v1/krankenkasse/entgeltbescheinigung-au should save a new registration" {
        val jsonResource = ClassPathResource("eingehend/Entgeltbescheinigung-Arbeitsunfähigkeit.json")
        val json = jsonResource.inputStream.bufferedReader().use { it.readText() }

        webTestClient.post()
            .uri("/v1/krankenkasse/entgeltbescheinigung-au")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(json)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.meldecenterId").exists()
            .jsonPath("$.meldung.id").isEqualTo("b8a85b87-1b5f-4f5d-abf2-c6c7969a4609")

        val saved = repository.findAll().toList()
        saved.any { it.meldungId.toString() == "b8a85b87-1b5f-4f5d-abf2-c6c7969a4609" } shouldBe true
    }

    "GET /v1/krankenkasse/entgeltbescheinigung-au should return paginated results" {
        // Given there is at least 1 entry from test data migration

        webTestClient.get()
            .uri("/v1/krankenkasse/entgeltbescheinigung-au?page=0&size=1&sort=meldecenterId,asc")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.length()").isEqualTo(1)
            .jsonPath("$[0].meldecenterId").isEqualTo("00000000-0000-0000-0000-000000000003")
    }
})
