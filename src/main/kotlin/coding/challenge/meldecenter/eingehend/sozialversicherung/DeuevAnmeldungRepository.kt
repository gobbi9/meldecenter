package coding.challenge.meldecenter.eingehend.sozialversicherung

import coding.challenge.meldecenter.shared.sozialversicherung.DeuevAnmeldungEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import java.util.UUID

/**
 * Repository für den Zugriff auf DEÜV-Anmeldungen.
 * Ermöglicht CRUD-Operationen auf der DEUEV_ANMELDUNG Tabelle.
 */
interface DeuevAnmeldungRepository :
    CoroutineCrudRepository<DeuevAnmeldungEntity, UUID>,
    CoroutineSortingRepository<DeuevAnmeldungEntity, UUID> {

    fun findAllBy(pageable: Pageable): Flow<DeuevAnmeldungEntity>
}
