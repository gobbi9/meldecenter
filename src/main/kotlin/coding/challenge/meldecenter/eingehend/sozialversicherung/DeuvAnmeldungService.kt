package coding.challenge.meldecenter.eingehend.sozialversicherung

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 * Service für die Verarbeitung von DEÜV-Anmeldungen.
 * Übernimmt die Speicherung der Anmeldungen.
 */
@Service
class DeuvAnmeldungService(
    private val deuevAnmeldungRepository: DeuevAnmeldungRepository,
) {

    /**
     * Speichert eine DEÜV-Anmeldung.
     * @param deuvAnmeldungDto Die zu speichernde Anmeldung als DTO.
     * @return Das gespeicherte DTO.
     */
    @NewSpan
    suspend fun save(deuvAnmeldungDto: DeuvAnmeldungDto): DeuvAnmeldungDto {
        log.debug { "Speichere DEÜV-Anmeldung mit ID: ${deuvAnmeldungDto.meldung.id}" }
        log.trace { "DTO Details: $deuvAnmeldungDto" }

        val savedDeuvAnmeldungEntity =
            deuevAnmeldungRepository.save(deuvAnmeldungDto.toEntity())

        log.debug { "DEÜV-Anmeldung erfolgreich gespeichert. Meldecenter-ID: ${savedDeuvAnmeldungEntity.meldecenterId}" }
        return savedDeuvAnmeldungEntity.toDto()
    }
}
