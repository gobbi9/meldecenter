package coding.challenge.meldecenter.ausgehend.export

import coding.challenge.meldecenter.ausgehend.export.event.ExportEventService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class ExportPreparerTest : StringSpec({

    "Prepare should return updated export and save start event" {
        val exportRepository = mockk<ExportRepository>()
        val exportEventService = mockk<ExportEventService>()
        val preparer = ExportPreparer(exportRepository, exportEventService)
        val export = mockk<ExportEntity>(relaxed = true) {
            every { id } returns 10L
        }
        val updatedExport = mockk<ExportEntity>(relaxed = true)

        coEvery { exportRepository.updateStatus(10L, ExportStatus.EXPORTING, ExportStatus.CREATED) } returns 1
        coEvery { exportRepository.findByIdAndStatus(10L, ExportStatus.EXPORTING) } returns updatedExport
        coEvery { exportEventService.saveStartEvent(10L) } returns Unit

        val result = preparer.prepare(export)

        result shouldBe updatedExport
        coVerify {
            exportRepository.updateStatus(10L, ExportStatus.EXPORTING, ExportStatus.CREATED)
            exportRepository.findByIdAndStatus(10L, ExportStatus.EXPORTING)
            exportEventService.saveStartEvent(10L)
        }
    }

    "Prepare should return null if update fails" {
        val exportRepository = mockk<ExportRepository>()
        val exportEventService = mockk<ExportEventService>()
        val preparer = ExportPreparer(exportRepository, exportEventService)
        val export = mockk<ExportEntity>(relaxed = true) {
            every { id } returns 10L
            every { status } returns ExportStatus.EXPORTED
        }

        coEvery { exportRepository.updateStatus(10L, ExportStatus.EXPORTING, ExportStatus.CREATED) } returns 0

        val result = preparer.prepare(export)

        result shouldBe null
    }
})
