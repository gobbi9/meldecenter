package coding.challenge.meldecenter.ausgehend.export

import coding.challenge.meldecenter.eingehend.shared.MeldungTyp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ExportFactoryTest : StringSpec({

    "NewExport should create ExportEntity with status CREATED" {
        val typ = MeldungTyp.DEUEV_ANMELDUNG
        val betriebsnummer = "12345678"

        val result = newExport(typ, betriebsnummer)

        result.typ shouldBe typ.toString()
        result.status shouldBe ExportStatus.CREATED
        result.betriebsnummer shouldBe betriebsnummer
    }
})
