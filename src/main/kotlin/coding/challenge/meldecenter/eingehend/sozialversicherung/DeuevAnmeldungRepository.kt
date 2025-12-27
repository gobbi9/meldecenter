package coding.challenge.meldecenter.eingehend.sozialversicherung

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface DeuevAnmeldungRepository : CoroutineCrudRepository<DeuevAnmeldungEntity, UUID>
