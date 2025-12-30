package coding.challenge.meldecenter.ausgehend.export

import coding.challenge.meldecenter.eingehend.shared.MeldungTyp
import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.time.Instant

@MeldecenterSpringBootTest
class ExportRepositoryIT(
    private val repository: ExportRepository,
) : StringSpec({

    "Should read placeholder export from migration" {
        val placeholder = repository.findById(1)
        placeholder.shouldNotBeNull()
        placeholder.id shouldBe 1
        placeholder.typ shouldBe MeldungTyp.MELDUNG
        placeholder.status shouldBe ExportStatus.DUPLICATES
        placeholder.betriebsNummer shouldBe "00000000"
        placeholder.traceId shouldBe "placeholder-trace-id"
    }

    "Should save and find ExportEntity" {
        val export = ExportEntity(
            typ = MeldungTyp.DEUEV_ANMELDUNG,
            status = ExportStatus.CREATED,
            betriebsNummer = "B12345678",
            traceId = "test-trace-id",
            createdBy = "test-user"
        )
        val savedExport = repository.save(export)
        savedExport.id.shouldNotBeNull()
        savedExport.id shouldBeGreaterThan 1

        val foundExport = repository.findById(savedExport.id)
        foundExport.shouldNotBeNull()
        foundExport.typ shouldBe MeldungTyp.DEUEV_ANMELDUNG
        foundExport.status shouldBe ExportStatus.CREATED
        foundExport.createdBy shouldBe "test-user"
    }

    "Should update ExportEntity" {
        val placeholder = repository.findById(1L)
        placeholder.shouldNotBeNull()
        val now = Instant.now()
        placeholder.updatedAt = now
        repository.save(placeholder)
        val updatedPlaceholder = repository.findById(placeholder.id)

        updatedPlaceholder.shouldNotBeNull()
        updatedPlaceholder.updatedAt shouldBe now
    }
})
