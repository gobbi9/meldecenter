package coding.challenge.meldecenter.ausgehend

import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.EntgeltbescheinigungenAuDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.outbox.RueckmeldungDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jakarta.xml.bind.JAXBContext
import java.io.StringReader

class KrankenkasseXmlMappingTest : StringSpec({

    "Should map KG_ENTG XML correctly to salary certificate DTO" {
        val xmlContent =
            javaClass
                .getResource("/ausgehend/ftp/inbox/KG_ENTG_12345678_20250310_01.xml")
                ?.readText()
        xmlContent.shouldNotBeNull()

        val context =
            JAXBContext.newInstance(EntgeltbescheinigungenAuDto::class.java)
        val unmarshaller = context.createUnmarshaller()
        val dto =
            unmarshaller.unmarshal(StringReader(xmlContent)) as EntgeltbescheinigungenAuDto

        dto.absender.betriebsnummer shouldBe "12345678"
        dto.bescheinigungen.size shouldBe 3
        dto.bescheinigungen[0].mitarbeiterId shouldBe "EMP-4452"
        dto.bescheinigungen[0].person.vorname shouldBe "Anna"
        dto.bescheinigungen[0].entgelt.bruttoentgelt shouldBe "3200.00"
    }

    "Should map KG_RUECK XML correctly to Outbox response DTO" {
        val xmlContent =
            javaClass
                .getResource("/ausgehend/ftp/outbox/KG_RUECK_12345678_20250310_01.xml")
                ?.readText()
        xmlContent.shouldNotBeNull()

        val context = JAXBContext.newInstance(RueckmeldungDto::class.java)
        val unmarshaller = context.createUnmarshaller()
        val dto =
            unmarshaller.unmarshal(StringReader(xmlContent)) as RueckmeldungDto

        dto.kopf.empfaenger shouldBe "KRANKENKASSE"
        dto.ergebnisse.size shouldBe 3
        dto.ergebnisse[0].status shouldBe "ANGENOMMEN"
        dto.ergebnisse[0].referenz.mitarbeiterId shouldBe "EMP-4452"
    }
})
