package coding.challenge.meldecenter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@MeldecenterSpringBootTest
class MeldecenterApplicationTests : StringSpec({
    "context loads" {
        1 shouldBe 1
    }
})
