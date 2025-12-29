package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.eingehend.shared.meldungen.EntgeltbescheinigungAuDto
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 * Service für die Verarbeitung von Entgeltbescheinigungen bei Arbeitsunfähigkeit.
 */
@Service
class EntgeltbescheinigungAuService(
    private val entgeltbescheinigungAuRepository: EntgeltbescheinigungAuRepository,
) {

    /**
     * Speichert eine Entgeltbescheinigung.
     * @param entgeltbescheinigungAuDto Die zu speichernde Bescheinigung als DTO.
     * @return Das gespeicherte DTO.
     */
    @NewSpan
    suspend fun save(
        entgeltbescheinigungAuDto: EntgeltbescheinigungAuDto
    ): EntgeltbescheinigungAuDto {
        log.debug { "Speichere Entgeltbescheinigung AU mit ID: ${entgeltbescheinigungAuDto.meldung.id}" }
        log.trace { "DTO Details: $entgeltbescheinigungAuDto" }

        val savedEntity =
            entgeltbescheinigungAuRepository.save(entgeltbescheinigungAuDto.toEntity())

        log.debug { "Entgeltbescheinigung AU erfolgreich gespeichert. Meldecenter-ID: ${savedEntity.meldecenterId}" }
        return savedEntity.toDto()
    }

    /**
     * Gibt alle Entgeltbescheinigungen paginiert zurück.
     * @param pageable Die Pagination-Informationen.
     * @return Ein Flow von Entgeltbescheinigungen.
     */
    @NewSpan
    fun findAll(pageable: Pageable): Flow<EntgeltbescheinigungAuDto> {
        log.debug { "Lade Entgeltbescheinigungen AU mit Pagination: $pageable" }
        return entgeltbescheinigungAuRepository
            .findAllBy(pageable)
            .map { it.toDto() }
    }
}
