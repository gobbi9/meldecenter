package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.shared.krankenkasse.EntgeltbescheinigungAuEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repository für den Zugriff auf [EntgeltbescheinigungAuEntity]-Daten in der Datenbank.
 */
@Repository
interface EntgeltbescheinigungAuRepository :
    CoroutineCrudRepository<EntgeltbescheinigungAuEntity, UUID>,
    CoroutineSortingRepository<EntgeltbescheinigungAuEntity, UUID> {

    /**
     * Findet alle Entgeltbescheinigungen mit Paginierung.
     *
     * @param pageable Die Paginierungsinformationen.
     * @return Ein [Flow] von [EntgeltbescheinigungAuEntity].
     */
    fun findAllBy(pageable: Pageable): Flow<EntgeltbescheinigungAuEntity>
}
