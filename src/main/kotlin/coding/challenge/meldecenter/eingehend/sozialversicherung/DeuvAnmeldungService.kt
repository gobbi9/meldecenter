package coding.challenge.meldecenter.eingehend.sozialversicherung

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 * Service für die Verarbeitung von DEÜV-Anmeldungen.
 * Übernimmt die Speicherung der Anmeldungen.
 */
@Service
class DeuvAnmeldungService(
    private val repository: DeuevAnmeldungRepository,
) {

    /**
     * Speichert eine DEÜV-Anmeldung.
     * @param dto Die zu speichernde Anmeldung als DTO.
     * @return Die gespeicherte Entity.
     */
    suspend fun save(dto: DeuvAnmeldungDto): DeuevAnmeldungEntity {
        log.debug { "Speichere DEÜV-Anmeldung mit ID: ${dto.meldung.id}" }
        log.trace { "DTO Details: $dto" }

        val entity = dto.toEntity()

        repository.save(entity)

        log.debug { "DEÜV-Anmeldung erfolgreich gespeichert. Meldecenter-ID: ${entity.meldecenterId}" }
        return entity
    }
}
