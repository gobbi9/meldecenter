package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.eingehend.shared.meldungen.DeuevAnmeldungDto
import coding.challenge.meldecenter.shared.sozialversicherung.DeuevAnmeldungEntity
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 * Service für die Verarbeitung von DEÜV-Anmeldungen.
 * Übernimmt die Speicherung der Anmeldungen.
 */
@Service
class DeuevAnmeldungService(
    private val deuevAnmeldungRepository: DeuevAnmeldungRepository,
) {

    /**
     * Speichert eine DEÜV-Anmeldung.
     * @param deuevAnmeldungDto Die zu speichernde Anmeldung als DTO.
     * @return Das gespeicherte DTO.
     */
    @NewSpan
    suspend fun save(deuevAnmeldungDto: DeuevAnmeldungDto): DeuevAnmeldungDto {
        log.debug { "Speichere DEÜV-Anmeldung mit ID: ${deuevAnmeldungDto.meldung.id}" }
        log.trace { "DTO Details: $deuevAnmeldungDto" }

        val savedDeuevAnmeldungEntity =
            deuevAnmeldungRepository.save(deuevAnmeldungDto.toEntity())

        log.debug { "DEÜV-Anmeldung erfolgreich gespeichert. Meldecenter-ID: ${savedDeuevAnmeldungEntity.meldecenterId}" }
        return savedDeuevAnmeldungEntity.toDto()
    }

    /**
     * Gibt alle DEÜV-Anmeldungen paginiert zurück.
     * @param pageable Die Pagination-Informationen.
     * @return Ein Flow von DEÜV-Anmeldungen.
     */
    @NewSpan
    fun findAll(pageable: Pageable): Flow<DeuevAnmeldungDto> {
        log.debug { "Lade DEÜV-Anmeldungen mit Pagination: $pageable" }
        return deuevAnmeldungRepository.findAllBy(pageable).map { it.toDto() }
    }
}
