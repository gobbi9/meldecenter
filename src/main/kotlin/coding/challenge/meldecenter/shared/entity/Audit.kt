package coding.challenge.meldecenter.shared.entity

import java.time.Instant

data class Audit(
    val createdAt: Instant = Instant.now(),
    val createdBy: String,
    var updatedAt: Instant,
    var updatedBy: String,
)
