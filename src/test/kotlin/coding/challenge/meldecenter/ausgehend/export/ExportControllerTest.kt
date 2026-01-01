package coding.challenge.meldecenter.ausgehend.export

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.springframework.http.HttpStatus

class ExportControllerTest : StringSpec({

    "Export should return ACCEPTED" {
        val exporter = mockk<Exporter>()
        val controller = ExportController(listOf(exporter))

        val response = controller.export()

        response.statusCode shouldBe HttpStatus.ACCEPTED
    }
})
