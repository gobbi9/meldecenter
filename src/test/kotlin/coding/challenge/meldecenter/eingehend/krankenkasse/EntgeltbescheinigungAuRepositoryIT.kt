package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import java.time.LocalDateTime
import java.util.UUID

@MeldecenterSpringBootTest
class EntgeltbescheinigungAuRepositoryIT(
    private val repository: EntgeltbescheinigungAuRepository,
) : StringSpec({
    extensions(SpringExtension)

    "Should read test migration data" {
        val bescheinigungen = repository.findAll().toList()
        bescheinigungen.size shouldBeGreaterThanOrEqual 1
    }

    "Should save new entity" {
        val existing = repository.findAll().first()
        val newId = UUID.randomUUID()
        val newEntity = existing.copy(meldecenterId = newId, version = null)
        repository.save(newEntity)
        val saved = repository.findById(newId)

        saved.shouldNotBeNull()
        saved.meldecenterId shouldBe newId
    }

    "Should update an entity" {
        val existing = repository.findAll().first()
        val now = LocalDateTime.now()
        existing.auditUpdatedAt = now
        repository.save(existing)
        val updated = repository.findById(existing.meldecenterId)

        updated.shouldNotBeNull()
        updated.auditUpdatedAt shouldBe now
    }
})
