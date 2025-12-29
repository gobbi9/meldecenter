package coding.challenge.meldecenter.ausgehend.ftp

import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File

@MeldecenterSpringBootTest
class FtpIntegrationIT(
    private val ftpService: FtpService
) : StringSpec({

    "should upload a xml file to the ftp server" {
        val localFile = File("src/test/resources/ausgehend/ftp/outbox/DEUEV-Anmeldung.xml")
        val remoteName = "DEUEV-Anmeldung-uploaded.xml"

        ftpService.uploadFile(localFile.absolutePath, remoteName)

        try {
            val content = ftpService.readFile(remoteName)
            content shouldBe localFile.readText()
        } finally {
            ftpService.deleteFile(remoteName)
        }
    }

    "should read a xml file from the ftp server" {
        val localFile = File("src/test/resources/ausgehend/ftp/inbox/DEUEV-Anmeldung.xml")
        val remoteName = "inbox/DEUEV-Anmeldung.xml"

        val content = ftpService.readFile(remoteName)
        content shouldBe localFile.readText()
    }
})
