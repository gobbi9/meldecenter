package coding.challenge.meldecenter.eingehend.sozialversicherung

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

/**
 * Repository für den Zugriff auf DEÜV-Anmeldungen.
 * Ermöglicht CRUD-Operationen auf der DEUEV_ANMELDUNG Tabelle.
 */
interface DeuevAnmeldungRepository :
    CoroutineCrudRepository<DeuevAnmeldungEntity, UUID>
