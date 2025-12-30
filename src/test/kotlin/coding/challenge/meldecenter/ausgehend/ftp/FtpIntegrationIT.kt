package coding.challenge.meldecenter.ausgehend.ftp

import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File

@MeldecenterSpringBootTest
class FtpIntegrationIT(
    private val ftpService: FtpService
) : StringSpec({

    "Should upload a xml file to the ftp server" {
        val localFile = File("src/test/resources/ausgehend/ftp/outbox/DEUEV_RUECK_12345678_20250201_0001.xml")
        val remoteName = "DEUEV_RUECK_12345678_20250201_0001-uploaded.xml"

        ftpService.uploadFile(localFile.absolutePath, remoteName)

        try {
            val content = ftpService.readFile(remoteName)
            content shouldBe localFile.readText()
        } finally {
            ftpService.deleteFile(remoteName)
        }
    }

    "Should read a xml file from the ftp server" {
        val localFile = File("src/test/resources/ausgehend/ftp/inbox/DEUEV_ANM_12345678_20250201_0001.xml")
        val remoteName = "inbox/DEUEV_ANM_12345678_20250201_0001.xml"

        val content = ftpService.readFile(remoteName)
        content shouldBe localFile.readText()
    }
})
