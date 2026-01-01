package coding.challenge.meldecenter.ausgehend.sozialversicherung.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportPreparer
import coding.challenge.meldecenter.ausgehend.export.ExportService
import coding.challenge.meldecenter.ausgehend.export.toXmlFilename
import coding.challenge.meldecenter.ausgehend.ftp.XmlFileUploader
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevAnmeldungenDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.newDeuevAnmeldungenXmlDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.flowOf

class DeuevAnmeldungExportUploaderTest : StringSpec({

    "Upload should prepare, create XML DTO and upload" {
        val exportPreparer = mockk<ExportPreparer>()
        val repository = mockk<DeuevAnmeldungExportRepository>()
        val xmlFileUploader = mockk<XmlFileUploader<DeuevAnmeldungenDto>>()
        val exportService = mockk<ExportService>()
        val uploader = DeuevAnmeldungExportUploader(exportPreparer, repository, xmlFileUploader, exportService)

        mockkStatic(ExportEntity::toXmlFilename, ::newDeuevAnmeldungenXmlDto)

        val unpreparedExport = mockk<ExportEntity>(relaxed = true)
        val export = mockk<ExportEntity>(relaxed = true) {
            every { id } returns 10L
        }
        val meldungXmlDto = mockk<DeuevAnmeldungenDto>(relaxed = true)
        val filename = "test.xml"

        coEvery { exportPreparer.prepare(unpreparedExport) } returns export
        every { repository.findByExportId(10L) } returns flowOf()
        every { newDeuevAnmeldungenXmlDto(export, any()) } returns meldungXmlDto
        every { export.toXmlFilename() } returns filename
        coEvery { xmlFileUploader.upload(meldungXmlDto, filename, 10L) } returns Unit
        coEvery { exportService.findById(10L) } returns export

        val result = uploader.upload(unpreparedExport)

        result shouldBe export
        coVerify {
            exportPreparer.prepare(unpreparedExport)
            repository.findByExportId(10L)
            xmlFileUploader.upload(meldungXmlDto, filename, 10L)
            exportService.findById(10L)
        }

        unmockkStatic(ExportEntity::toXmlFilename, ::newDeuevAnmeldungenXmlDto)
    }

    "Upload should return null if preparation fails" {
        val exportPreparer = mockk<ExportPreparer>()
        val repository = mockk<DeuevAnmeldungExportRepository>()
        val xmlFileUploader = mockk<XmlFileUploader<DeuevAnmeldungenDto>>()
        val exportService = mockk<ExportService>()
        val uploader = DeuevAnmeldungExportUploader(exportPreparer, repository, xmlFileUploader, exportService)

        val unpreparedExport = mockk<ExportEntity>(relaxed = true)
        coEvery { exportPreparer.prepare(unpreparedExport) } returns null

        val result = uploader.upload(unpreparedExport)

        result shouldBe null
    }
})
