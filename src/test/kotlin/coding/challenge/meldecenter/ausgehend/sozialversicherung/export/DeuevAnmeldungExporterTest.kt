package coding.challenge.meldecenter.ausgehend.sozialversicherung.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList

class DeuevAnmeldungExporterTest : StringSpec({

    "Export should process all export groups" {
        val repository = mockk<DeuevAnmeldungExportRepository>()
        val exportCreator = mockk<DeuevAnmeldungExportCreator>()
        val exportUploader = mockk<DeuevAnmeldungExportUploader>()
        val exporter = DeuevAnmeldungExporter(repository, exportCreator, exportUploader)
        val group1 = DeuevAnmeldungExportGroup("B1", 1)
        val group2 = DeuevAnmeldungExportGroup("B2", 2)
        val export1 = mockk<ExportEntity>(relaxed = true)
        val export2 = mockk<ExportEntity>(relaxed = true)

        every { repository.findAllExportGroups() } returns flowOf(group1, group2)
        coEvery { exportCreator.assign(group1) } returns export1
        coEvery { exportCreator.assign(group2) } returns export2
        coEvery { exportUploader.upload(export1) } returns export1
        coEvery { exportUploader.upload(export2) } returns export2

        val result = exporter.export().toList()

        result.size shouldBe 2
        result shouldBe listOf(export1, export2)
    }

    "Export should skip nulls from creator or uploader" {
        val repository = mockk<DeuevAnmeldungExportRepository>()
        val exportCreator = mockk<DeuevAnmeldungExportCreator>()
        val exportUploader = mockk<DeuevAnmeldungExportUploader>()
        val exporter = DeuevAnmeldungExporter(repository, exportCreator, exportUploader)
        val group1 = DeuevAnmeldungExportGroup("B1", 1)
        val group2 = DeuevAnmeldungExportGroup("B2", 2)
        val export1 = mockk<ExportEntity>(relaxed = true)

        every { repository.findAllExportGroups() } returns flowOf(group1, group2)
        coEvery { exportCreator.assign(group1) } returns export1
        coEvery { exportCreator.assign(group2) } returns null
        coEvery { exportUploader.upload(export1) } returns null

        val result = exporter.export().toList()

        result.size shouldBe 0
    }
})
