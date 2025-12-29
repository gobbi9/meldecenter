package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.eingehend.shared.meldungen.EntgeltbescheinigungAuDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.PageRequest

/**
 * Test für [EntgeltbescheinigungAuController].
 */
class EntgeltbescheinigungAuControllerTest : StringSpec({

    "EntgeltbescheinigungAuController.save calls service" {
        val dto = mockk<EntgeltbescheinigungAuDto>()
        val service = mockk<EntgeltbescheinigungAuService>()
        val controller = EntgeltbescheinigungAuController(service)

        coEvery { service.save(dto) } returns dto

        val result = controller.save(dto)

        result shouldBe dto

        coVerify(exactly = 1) {
            service.save(dto)
        }
    }

    "EntgeltbescheinigungAuController.getPage calls service" {
        val dto = mockk<EntgeltbescheinigungAuDto>()
        val service = mockk<EntgeltbescheinigungAuService>()
        val controller = EntgeltbescheinigungAuController(service)
        val pageable = PageRequest.of(0, 10)

        every { service.findAll(pageable) } returns flowOf(dto)

        val result = controller.getPage(pageable).toList()

        result.size shouldBe 1
        result.first() shouldBe dto

        verify(exactly = 1) {
            service.findAll(pageable)
        }
    }
})
