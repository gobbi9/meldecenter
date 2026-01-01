package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import java.time.LocalDateTime
import java.util.UUID

@MeldecenterSpringBootTest
class DeuevAnmeldungRepositoryIT(
    private val repository: DeuevAnmeldungRepository,
) : StringSpec({

    "Should read test migration data" {
        val anmeldungen = repository.findAll().toList()
        anmeldungen.size shouldBeGreaterThanOrEqual 2

        val firstAnmeldung = anmeldungen.find { it.meldecenterId == UUID.fromString("00000000-0000-0000-0000-000000000001") }
        firstAnmeldung.shouldNotBeNull()
        firstAnmeldung.exportId shouldBe 1L
    }

    "Should save new entity" {
        val anmeldung = repository.findAll().first()
        val newId = UUID.randomUUID()
        val newEntity = anmeldung.copy(meldecenterId = newId, version = null)
        repository.save(newEntity)
        val savedAnmeldung = repository.findById(newId)

        savedAnmeldung.shouldNotBeNull()
        savedAnmeldung.meldecenterId shouldBe newId
    }

    "Should update an entity" {
        val anmeldung = repository.findAll().first()
        val now = LocalDateTime.now()
        anmeldung.auditUpdatedAt = now
        repository.save(anmeldung)
        val updatedAnmeldung = repository.findById(anmeldung.meldecenterId)

        updatedAnmeldung.shouldNotBeNull()
        updatedAnmeldung.auditUpdatedAt shouldBe now
    }
})
