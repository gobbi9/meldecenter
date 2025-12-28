package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.eingehend.shared.meldungen.DeuevAnmeldungDto
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
 * Test für [DeuevAnmeldungController]
 */
class DeuevAnmeldungControllerTest : StringSpec({

    "DeuevAnmeldungController.postDeuevAnmeldung calls service" {
        val deuevAnmeldungDto = mockk<DeuevAnmeldungDto>()
        val deuevAnmeldungService = mockk<DeuevAnmeldungService>()
        val deuevAnmeldungController = DeuevAnmeldungController(deuevAnmeldungService)

        coEvery { deuevAnmeldungService.save(deuevAnmeldungDto) } returns deuevAnmeldungDto

        val result = deuevAnmeldungController.postDeuevAnmeldung(deuevAnmeldungDto)

        result shouldBe deuevAnmeldungDto

        coVerify(exactly = 1) {
            deuevAnmeldungService.save(deuevAnmeldungDto)
        }
    }

    "DeuevAnmeldungController.getDeuevAnmeldungen calls service" {
        val deuevAnmeldungDto = mockk<DeuevAnmeldungDto>()
        val deuevAnmeldungService = mockk<DeuevAnmeldungService>()
        val deuevAnmeldungController = DeuevAnmeldungController(deuevAnmeldungService)
        val pageable = PageRequest.of(0, 10)

        every { deuevAnmeldungService.findAll(pageable) } returns flowOf(deuevAnmeldungDto)

        val result = deuevAnmeldungController.getDeuevAnmeldungen(pageable).toList()

        result.size shouldBe 1
        result.first() shouldBe deuevAnmeldungDto

        verify(exactly = 1) {
            deuevAnmeldungService.findAll(pageable)
        }
    }
})
