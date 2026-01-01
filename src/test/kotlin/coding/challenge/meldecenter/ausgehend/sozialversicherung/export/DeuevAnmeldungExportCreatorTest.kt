package coding.challenge.meldecenter.ausgehend.sozialversicherung.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class DeuevAnmeldungExportCreatorTest : StringSpec({

    "Assign should create export and return it if items are assigned" {
        val exportAssigner = mockk<DeuevAnmeldungExportAssigner>()
        val exportService = mockk<ExportService>()
        val creator = DeuevAnmeldungExportCreator(exportAssigner, exportService)
        val exportGroup = DeuevAnmeldungExportGroup("B123", 5)
        val export = mockk<ExportEntity>(relaxed = true) {
            every { id } returns 10L
        }

        coEvery { exportService.insert(any()) } returns export
        coEvery { exportAssigner.deduplicateAndAssignToExport("B123", 10L) } returns 5

        val result = creator.assign(exportGroup)

        result shouldBe export
        coVerify {
            exportService.insert(withArg {
                it.betriebsnummer shouldBe "B123"
            })
            exportAssigner.deduplicateAndAssignToExport("B123", 10L)
        }
    }

    "Assign should delete export and return null if no items are assigned" {
        val exportAssigner = mockk<DeuevAnmeldungExportAssigner>()
        val exportService = mockk<ExportService>()
        val creator = DeuevAnmeldungExportCreator(exportAssigner, exportService)
        val exportGroup = DeuevAnmeldungExportGroup("B123", 0)
        val export = mockk<ExportEntity>(relaxed = true) {
            every { id } returns 10L
        }

        coEvery { exportService.insert(any()) } returns export
        coEvery { exportAssigner.deduplicateAndAssignToExport("B123", 10L) } returns 0
        coEvery { exportService.nullAndDeleteUnassignedExport(10L) } returns null

        val result = creator.assign(exportGroup)

        result shouldBe null
        coVerify {
            exportService.insert(withArg {
                it.betriebsnummer shouldBe "B123"
            })
            exportAssigner.deduplicateAndAssignToExport("B123", 10L)
            exportService.nullAndDeleteUnassignedExport(10L)
        }
    }
})
