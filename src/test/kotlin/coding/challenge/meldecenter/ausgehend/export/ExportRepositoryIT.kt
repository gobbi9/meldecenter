package coding.challenge.meldecenter.ausgehend.export

import coding.challenge.meldecenter.eingehend.shared.MeldungTyp
import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

@MeldecenterSpringBootTest
class ExportRepositoryIT(
    private val repository: ExportRepository,
) : StringSpec({

    "Should read placeholder export from migration" {
        val placeholder = repository.findById(1)
        placeholder.shouldNotBeNull()
        placeholder.id shouldBe 1
        placeholder.typ shouldBe MeldungTyp.MELDUNG.toString()
        placeholder.status shouldBe ExportStatus.DUPLICATES
        placeholder.betriebsnummer shouldBe "00000000"
        placeholder.traceId shouldBe "placeholder-trace-id"
    }

    "Should save and find ExportEntity" {
        val export = ExportEntity(
            typ = MeldungTyp.DEUEV_ANMELDUNG.toString(),
            status = ExportStatus.CREATED,
            betriebsnummer = "B12345678",
            traceId = "test-trace-id",
            createdBy = "test-user"
        )
        val savedExport = repository.save(export)
        savedExport.id.shouldNotBeNull()
        savedExport.id shouldBeGreaterThan 1

        val foundExport = repository.findById(savedExport.id)
        foundExport.shouldNotBeNull()
        foundExport.typ shouldBe MeldungTyp.DEUEV_ANMELDUNG.toString()
        foundExport.status shouldBe ExportStatus.CREATED
        foundExport.createdBy shouldBe "test-user"
    }

    "Should update ExportEntity" {
        val placeholder = repository.findById(1L)
        placeholder.shouldNotBeNull()
        val now = LocalDateTime.now()
        placeholder.updatedAt = now
        repository.save(placeholder)
        val updatedPlaceholder = repository.findById(placeholder.id)

        updatedPlaceholder.shouldNotBeNull()
        updatedPlaceholder.updatedAt shouldBe now
    }
})
