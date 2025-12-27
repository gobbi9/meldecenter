package coding.challenge.meldecenter

import coding.challenge.meldecenter.eingehend.sozialversicherung.DeuevAnmeldungRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.toList
import java.time.Instant

@MeldecenterSpringBootTest
class DeuevAnmeldungIT(
    private val repository: DeuevAnmeldungRepository
) : StringSpec({

    "Should read test migration data, update audit.updatedAt and save" {
        // Read records inserted by migration
        val anmeldungen = repository.findAll().toList()
        anmeldungen.size shouldBe 2

        // Update audit.updatedAt
        val now = Instant.now()
        val updatedAnmeldungen = anmeldungen.map {
            it.auditUpdatedAt = now
            it
        }

        // Save them
        repository.saveAll(updatedAnmeldungen).toList()

        // Verify updates
        val savedAnmeldungen = repository.findAll().toList()
        savedAnmeldungen.size shouldBe 2
        savedAnmeldungen.forEach {
            it.auditUpdatedAt shouldBe now
        }
    }
})
