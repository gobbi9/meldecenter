package coding.challenge.meldecenter.ausgehend.sozialversicherung.export

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class DeuevAnmeldungExportAssignerTest : StringSpec({

    val repository = mockk<DeuevAnmeldungExportRepository>()
    val assigner = DeuevAnmeldungExportAssigner(repository)

    "DeduplicateAndAssignToExport should call repository deduplicate and assignToExport" {
        val betriebsnummer = "B123"
        val exportId = 10L
        coEvery { repository.deduplicate(betriebsnummer) } returns 2
        coEvery { repository.assignToExport(betriebsnummer, exportId) } returns 3

        val result = assigner.deduplicateAndAssignToExport(betriebsnummer, exportId)

        result shouldBe 3
        coVerify {
            repository.deduplicate(betriebsnummer)
            repository.assignToExport(betriebsnummer, exportId)
        }
    }
})
