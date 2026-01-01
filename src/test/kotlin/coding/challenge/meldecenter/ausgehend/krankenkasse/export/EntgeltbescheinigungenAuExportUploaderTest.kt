package coding.challenge.meldecenter.ausgehend.krankenkasse.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportPreparer
import coding.challenge.meldecenter.ausgehend.export.ExportService
import coding.challenge.meldecenter.ausgehend.export.toXmlFilename
import coding.challenge.meldecenter.ausgehend.ftp.XmlFileUploader
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.EntgeltbescheinigungenAuDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.newEntgeltbescheinigungenAuXmlDto
import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltbescheinigungAuRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.flowOf

class EntgeltbescheinigungenAuExportUploaderTest : StringSpec({

    "Upload should prepare, create XML DTO and upload" {
        val exportPreparer = mockk<ExportPreparer>()
        val repository = mockk<EntgeltbescheinigungAuRepository>()
        val xmlFileUploader = mockk<XmlFileUploader<EntgeltbescheinigungenAuDto>>()
        val exportService = mockk<ExportService>()
        val uploader = EntgeltbescheinigungenAuExportUploader(exportPreparer, repository, xmlFileUploader, exportService)

        mockkStatic(ExportEntity::toXmlFilename, ::newEntgeltbescheinigungenAuXmlDto)

        val unpreparedExport = mockk<ExportEntity>(relaxed = true)
        val export = mockk<ExportEntity>(relaxed = true) {
            every { id } returns 10L
        }
        val meldungXmlDto = mockk<EntgeltbescheinigungenAuDto>(relaxed = true)
        val filename = "test.xml"

        coEvery { exportPreparer.prepare(unpreparedExport) } returns export
        every { repository.findByExportId(10L) } returns flowOf()
        every { newEntgeltbescheinigungenAuXmlDto(export, any()) } returns meldungXmlDto
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

        unmockkStatic(ExportEntity::toXmlFilename, ::newEntgeltbescheinigungenAuXmlDto)
    }

    "Upload should return null if preparation fails" {
        val exportPreparer = mockk<ExportPreparer>()
        val repository = mockk<EntgeltbescheinigungAuRepository>()
        val xmlFileUploader = mockk<XmlFileUploader<EntgeltbescheinigungenAuDto>>()
        val exportService = mockk<ExportService>()
        val uploader = EntgeltbescheinigungenAuExportUploader(exportPreparer, repository, xmlFileUploader, exportService)

        val unpreparedExport = mockk<ExportEntity>(relaxed = true)
        coEvery { exportPreparer.prepare(unpreparedExport) } returns null

        val result = uploader.upload(unpreparedExport)

        result shouldBe null
    }
})
