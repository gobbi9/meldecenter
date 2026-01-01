package coding.challenge.meldecenter.ausgehend.ftp

import coding.challenge.meldecenter.ausgehend.export.ExportService
import coding.challenge.meldecenter.ausgehend.export.ExportStatus
import coding.challenge.meldecenter.ausgehend.export.event.ExportEventService
import io.kotest.core.spec.style.StringSpec
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "TestDto")
data class TestDto(var name: String = "")

class XmlFileUploaderTest : StringSpec({

    val ftpService = mockk<FtpService>()
    val exportService = mockk<ExportService>()
    val exportEventService = mockk<ExportEventService>()
    val uploader = XmlFileUploader<TestDto>(ftpService, exportService, exportEventService)

    beforeTest {
        clearMocks(ftpService, exportService, exportEventService)
    }

    "Upload should succeed and update status to EXPORTED" {
        val dto = TestDto("test")
        val filename = "test.xml"
        val exportId = 1L

        every { ftpService.uploadFile(any(), any()) } returns Unit
        coEvery { exportService.updateStatus(any(), any()) } returns Unit
        coEvery { exportEventService.saveEndEvent(any()) } returns Unit

        uploader.upload(dto, filename, exportId)

        coVerify(exactly = 1) {
            ftpService.uploadFile(any(), filename)
            exportService.updateStatus(exportId, ExportStatus.EXPORTED)
            exportEventService.saveEndEvent(exportId)
        }
    }

    "Upload should fail and update status to FAILED" {
        val dto = TestDto("test")
        val filename = "test.xml"
        val exportId = 1L
        val error = RuntimeException("FTP upload failed")

        every { ftpService.uploadFile(any(), any()) } throws error
        coEvery { exportService.updateStatus(any(), any()) } returns Unit
        coEvery { exportEventService.saveErrorEvent(any(), any()) } returns Unit

        uploader.upload(dto, filename, exportId)

        coVerify(exactly = 1) {
            exportService.updateStatus(exportId, ExportStatus.FAILED)
            exportEventService.saveErrorEvent(exportId, any())
        }
    }
})
