package coding.challenge.meldecenter.ausgehend.ftp

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory
import org.springframework.integration.ftp.session.FtpSession
import java.io.InputStream
import java.io.OutputStream
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists

class FtpServiceTest : StringSpec({

    val sessionFactory = mockk<DefaultFtpSessionFactory>()
    val session = mockk<FtpSession>(relaxed = true)
    val ftpService = FtpService(sessionFactory)

    beforeTest {
        every { sessionFactory.session } returns session
    }

    "UploadFile should write to session" {
        val tempFile = createTempFile("ftp-test")
        try {
            ftpService.uploadFile(tempFile.toString(), "remote.xml")
            verify { session.write(any<InputStream>(), "remote.xml") }
        } finally {
            tempFile.deleteIfExists()
        }
    }

    "ReadFile should read from session" {
        every { session.read("remote.xml", any<OutputStream>()) } answers {
            val os = it.invocation.args[1] as OutputStream
            os.write("content".toByteArray())
        }

        val result = ftpService.readFile("remote.xml")

        result shouldBe "content"
    }

    "DeleteFile should remove from session" {
        every { session.remove("remote.xml") } returns true

        val result = ftpService.deleteFile("remote.xml")

        result shouldBe true
        verify { session.remove("remote.xml") }
    }
})
