package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.eingehend.shared.meldungen.EntgeltbescheinigungAuDto
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
 * Test für [EntgeltbescheinigungAuService].
 */
class EntgeltbescheinigungAuServiceTest : StringSpec({

    "EntgeltbescheinigungAuService calls repository and mappers" {
        mockkStatic(EntgeltbescheinigungAuEntity::toDto, EntgeltbescheinigungAuDto::toEntity)

        val entity = mockk<EntgeltbescheinigungAuEntity>(relaxed = true)
        val dto = mockk<EntgeltbescheinigungAuDto>(relaxed = true)
        val repository = mockk<EntgeltbescheinigungAuRepository> {
            coEvery { save(entity) } returns entity
        }
        val service = EntgeltbescheinigungAuService(repository)

        every { entity.toDto() } returns dto
        every { dto.toEntity() } returns entity

        val savedDto = service.save(dto)
        savedDto.shouldNotBeNull()

        coVerify(exactly = 1) {
            dto.toEntity()
            repository.save(entity)
            entity.toDto()
        }

        unmockkStatic(EntgeltbescheinigungAuEntity::toDto, EntgeltbescheinigungAuDto::toEntity)
    }

    "EntgeltbescheinigungAuService.findAll calls repository and mappers" {
        mockkStatic(EntgeltbescheinigungAuEntity::toDto)

        val entity = mockk<EntgeltbescheinigungAuEntity>(relaxed = true)
        val dto = mockk<EntgeltbescheinigungAuDto>(relaxed = true)
        val repository = mockk<EntgeltbescheinigungAuRepository>()
        val pageable = PageRequest.of(0, 10)

        every { repository.findAllBy(pageable) } returns flowOf(entity)
        every { entity.toDto() } returns dto

        val service = EntgeltbescheinigungAuService(repository)

        val result = service.findAll(pageable).toList()

        result.size shouldBe 1
        result.first() shouldBe dto

        verify(exactly = 1) {
            repository.findAllBy(pageable)
            entity.toDto()
        }

        unmockkStatic(EntgeltbescheinigungAuEntity::toDto)
    }
})
