package coding.challenge.meldecenter.ausgehend.export.event

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micrometer.tracing.Span
import io.micrometer.tracing.TraceContext
import io.micrometer.tracing.Tracer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class ExportEventServiceTest : StringSpec({

    "SaveExportEvent should call repository save" {
        val exportEventRepository = mockk<ExportEventRepository>()
        val tracer = mockk<Tracer>()
        val exportEventService = ExportEventService(exportEventRepository, tracer)
        val event = mockk<ExportEventEntity>(relaxed = true)
        coEvery { exportEventRepository.save(event) } returns event

        exportEventService.saveExportEvent(event)

        coVerify(exactly = 1) { exportEventRepository.save(event) }
    }

    "SaveStartEvent should create and save START event" {
        val exportEventRepository = mockk<ExportEventRepository>()
        val tracer = mockk<Tracer>()
        val exportEventService = ExportEventService(exportEventRepository, tracer)
        val span = mockk<Span>(relaxed = true)
        val context = mockk<TraceContext>(relaxed = true)
        every { tracer.currentSpan() } returns span
        every { span.context() } returns context
        every { context.traceId() } returns "trace-123"

        coEvery { exportEventRepository.save(any()) } returns mockk(relaxed = true)

        exportEventService.saveStartEvent(1L)

        coVerify(exactly = 1) {
            exportEventRepository.save(withArg {
                it.exportId shouldBe 1L
                it.type shouldBe ExportEventType.START
                it.traceId shouldBe "trace-123"
            })
        }
    }

    "SaveEndEvent should create and save END event" {
        val exportEventRepository = mockk<ExportEventRepository>()
        val tracer = mockk<Tracer>()
        val exportEventService = ExportEventService(exportEventRepository, tracer)
        val span = mockk<Span>(relaxed = true)
        val context = mockk<TraceContext>(relaxed = true)
        every { tracer.currentSpan() } returns span
        every { span.context() } returns context
        every { context.traceId() } returns "trace-123"

        coEvery { exportEventRepository.save(any()) } returns mockk(relaxed = true)

        exportEventService.saveEndEvent(1L)

        coVerify(exactly = 1) {
            exportEventRepository.save(withArg {
                it.exportId shouldBe 1L
                it.type shouldBe ExportEventType.END
                it.traceId shouldBe "trace-123"
            })
        }
    }

    "SaveErrorEvent should create and save ERROR event" {
        val exportEventRepository = mockk<ExportEventRepository>()
        val tracer = mockk<Tracer>()
        val exportEventService = ExportEventService(exportEventRepository, tracer)
        val span = mockk<Span>(relaxed = true)
        val context = mockk<TraceContext>(relaxed = true)
        every { tracer.currentSpan() } returns span
        every { span.context() } returns context
        every { context.traceId() } returns "trace-123"

        coEvery { exportEventRepository.save(any()) } returns mockk(relaxed = true)
        val error = RuntimeException("error message")

        exportEventService.saveErrorEvent(1L, error)

        coVerify(exactly = 1) {
            exportEventRepository.save(withArg {
                it.exportId shouldBe 1L
                it.type shouldBe ExportEventType.ERROR
                it.details shouldBe "error message"
                it.traceId shouldBe "trace-123"
            })
        }
    }
})
