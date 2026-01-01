package coding.challenge.meldecenter.ausgehend.export.event

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ExportEventFactoryTest : StringSpec({

    "NewExportEvent should create ExportEventEntity with given values" {
        val exportId = 1L
        val type = ExportEventType.START
        val details = "Some details"
        val traceId = "trace-123"

        val result = newExportEvent(exportId, type, details, traceId)

        result.exportId shouldBe exportId
        result.type shouldBe type
        result.details shouldBe details
        result.traceId shouldBe traceId
        result.createdBy shouldBe "SYSTEM"
    }

    "newExportEvent should truncate details to 1000 characters" {
        val longDetails = "a".repeat(1001)
        val result = newExportEvent(1L, ExportEventType.ERROR, longDetails, "trace-123")

        result.details?.length shouldBe 1000
    }
})
