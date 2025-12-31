package coding.challenge.meldecenter.ausgehend.export

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.util.UUID

class ExportMapperTest : StringSpec({

    "toXmlFilename should return correctly formatted filename" {
        val createdAt = LocalDateTime.of(2025, 12, 31, 10, 0)
        val export = ExportEntity(
            id = 123L,
            typ = "KG_ENTG",
            status = ExportStatus.CREATED,
            betriebsnummer = "12345678",
            createdAt = createdAt
        )

        val result = export.toXmlFilename()

        result shouldBe "KG_ENTG_12345678_20251231_123.xml"
    }
})
