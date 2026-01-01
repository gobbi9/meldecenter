package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.eingehend.shared.meldungen.DeuevAnmeldungDto
import coding.challenge.meldecenter.shared.sozialversicherung.DeuevAnmeldungEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.PageRequest

/**
 * Test für [DeuevAnmeldungService]
 */
class DeuevAnmeldungServiceTest : StringSpec({

    "DeuevAnmeldungService calls repository and mappers" {
        mockkStatic(DeuevAnmeldungEntity::toDto, DeuevAnmeldungDto::toEntity)

        val deuevAnmeldungEntity = mockk<DeuevAnmeldungEntity>(relaxed = true)
        val deuevAnmeldungDto = mockk<DeuevAnmeldungDto>(relaxed = true)
        val deuevAnmeldungRepository = mockk<DeuevAnmeldungRepository> {
            coEvery { save(deuevAnmeldungEntity) } returns deuevAnmeldungEntity
        }
        val deuevAnmeldungService = DeuevAnmeldungService(deuevAnmeldungRepository)

        every { deuevAnmeldungEntity.toDto() } returns deuevAnmeldungDto
        every { deuevAnmeldungDto.toEntity() } returns deuevAnmeldungEntity

        val savedDeuevAnmeldungDto = deuevAnmeldungService.save(deuevAnmeldungDto)
        savedDeuevAnmeldungDto.shouldNotBeNull()

        coVerify(exactly = 1) {
            deuevAnmeldungDto.toEntity()
            deuevAnmeldungRepository.save(deuevAnmeldungEntity)
            deuevAnmeldungEntity.toDto()
        }

        unmockkStatic(DeuevAnmeldungEntity::toDto, DeuevAnmeldungDto::toEntity)
    }

    "DeuevAnmeldungService.findAll calls repository and mappers" {
        mockkStatic(DeuevAnmeldungEntity::toDto)

        val deuevAnmeldungEntity = mockk<DeuevAnmeldungEntity>(relaxed = true)
        val deuevAnmeldungDto = mockk<DeuevAnmeldungDto>(relaxed = true)
        val deuevAnmeldungRepository = mockk<DeuevAnmeldungRepository>()
        val pageable = PageRequest.of(0, 10)

        every { deuevAnmeldungRepository.findAllBy(pageable) } returns flowOf(deuevAnmeldungEntity)
        every { deuevAnmeldungEntity.toDto() } returns deuevAnmeldungDto

        val deuevAnmeldungService = DeuevAnmeldungService(deuevAnmeldungRepository)

        val result = deuevAnmeldungService.findAll(pageable).toList()

        result.size shouldBe 1
        result.first() shouldBe deuevAnmeldungDto

        verify(exactly = 1) {
            deuevAnmeldungRepository.findAllBy(pageable)
            deuevAnmeldungEntity.toDto()
        }

        unmockkStatic(DeuevAnmeldungEntity::toDto)
    }
})
