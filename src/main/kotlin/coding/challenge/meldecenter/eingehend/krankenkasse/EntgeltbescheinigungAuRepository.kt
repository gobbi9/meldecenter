package coding.challenge.meldecenter.eingehend.krankenkasse

import org.springframework.data.domain.Pageable
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repository für [EntgeltbescheinigungAuEntity].
 */
@Repository
interface EntgeltbescheinigungAuRepository :
    CoroutineCrudRepository<EntgeltbescheinigungAuEntity, UUID>,
    CoroutineSortingRepository<EntgeltbescheinigungAuEntity, UUID> {

    fun findAllBy(pageable: Pageable): Flow<EntgeltbescheinigungAuEntity>
}
