package coding.challenge.meldecenter

import coding.challenge.meldecenter.eingehend.sozialversicherung.DeuevAnmeldungRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import java.util.UUID

@MeldecenterSpringBootTest
class DeuevAnmeldungRepositoryIT(
    private val repository: DeuevAnmeldungRepository,
) : StringSpec({

    "Should read test migration data" {
        val anmeldungen = repository.findAll().toList()
        anmeldungen.size shouldBe 2
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
})
