package coding.challenge.meldecenter.ausgehend.export

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class ExportServiceTest : StringSpec({

    "Insert should save and return export" {
        val exportRepository = mockk<ExportRepository>()
        val exportService = ExportService(exportRepository)
        val export = mockk<ExportEntity>(relaxed = true)
        coEvery { exportRepository.save(export) } returns export

        val result = exportService.insert(export)

        result shouldBe export
        coVerify(exactly = 1) { exportRepository.save(export) }
    }

    "NullAndDeleteUnassignedExport should delete and return null" {
        val exportRepository = mockk<ExportRepository>()
        val exportService = ExportService(exportRepository)
        val exportId = 1L
        coEvery { exportRepository.deleteById(exportId) } returns Unit

        val result = exportService.nullAndDeleteUnassignedExport(exportId)

        result shouldBe null
        coVerify(exactly = 1) { exportRepository.deleteById(exportId) }
    }

    "FindById should return export from repository" {
        val exportRepository = mockk<ExportRepository>()
        val exportService = ExportService(exportRepository)
        val exportId = 1L
        val export = mockk<ExportEntity>(relaxed = true)
        coEvery { exportRepository.findById(exportId) } returns export

        val result = exportService.findById(exportId)

        result shouldBe export
        coVerify(exactly = 1) { exportRepository.findById(exportId) }
    }

    "updateStatus should call repository updateStatus" {
        val exportRepository = mockk<ExportRepository>()
        val exportService = ExportService(exportRepository)
        val exportId = 1L
        val status = ExportStatus.EXPORTED
        coEvery {
            exportRepository.updateStatus(exportId, status, ExportStatus.EXPORTING)
        } returns 1

        exportService.updateStatus(exportId, status)

        coVerify(exactly = 1) {
            exportRepository.updateStatus(exportId, status, ExportStatus.EXPORTING)
        }
    }
})
