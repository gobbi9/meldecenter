package coding.challenge.meldecenter.eingehend.shared.meldungen

import com.fasterxml.jackson.annotation.JsonTypeInfo

/** Eine generische Meldung */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
sealed interface Meldung
