package coding.challenge.meldecenter.ausgehend

import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevAnmeldungenDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jakarta.xml.bind.JAXBContext
import java.io.StringReader
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.error.RueckmeldungDto as ErrorRueckmeldungDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.outbox.RueckmeldungDto as OutboxRueckmeldungDto

class SozialversicherungXmlMappingTest : StringSpec({

    "Should map DEUEV_ANM XML correctly to DEUEV registration DTO" {
        val xmlContent =
            javaClass
                .getResource("/ausgehend/ftp/inbox/DEUEV_ANM_12345678_20250201_0001.xml")
                ?.readText()
        xmlContent.shouldNotBeNull()

        val context = JAXBContext.newInstance(DeuevAnmeldungenDto::class.java)
        val unmarshaller = context.createUnmarshaller()
        val dto =
            unmarshaller.unmarshal(StringReader(xmlContent!!)) as DeuevAnmeldungenDto

        dto.absender.betriebsnummer shouldBe "12345678"
        dto.anmeldungen.size shouldBe 2
        dto.anmeldungen[0].mitarbeiterId shouldBe "EMP-9981"
        dto.anmeldungen[0].person.vorname shouldBe "Max"
        dto.anmeldungen[0].beschaeftigung.art shouldBe "SOZIALVERSICHERUNGSPFLICHTIG"
    }

    "Should map DEUEV_RUECK XML correctly to Outbox response DTO" {
        val xmlContent =
            javaClass
                .getResource("/ausgehend/ftp/outbox/DEUEV_RUECK_12345678_20250201_0001.xml")
                ?.readText()
        xmlContent.shouldNotBeNull()

        val context = JAXBContext.newInstance(OutboxRueckmeldungDto::class.java)
        val unmarshaller = context.createUnmarshaller()
        val dto =
            unmarshaller.unmarshal(StringReader(xmlContent!!)) as OutboxRueckmeldungDto

        dto.kopf.empfaenger shouldBe "SOZIALVERSICHERUNG"
        dto.kopf.verfahren shouldBe "DEUEV"
        dto.ergebnisse?.size shouldBe 2
        dto.ergebnisse?.get(0)?.status shouldBe "ANGENOMMEN"
        dto.ergebnisse?.get(1)?.status shouldBe "ABGELEHNT"
        dto.ergebnisse?.get(1)?.fehler?.code shouldBe "DEUEV-103"
    }

    "Should map DEUEV_ERR XML correctly to error response DTO" {
        val xmlContent =
            javaClass
                .getResource("/ausgehend/ftp/error/DEUEV_ERR_11223344_20250620_0001.xml")
                ?.readText()
        xmlContent.shouldNotBeNull()

        val context = JAXBContext.newInstance(ErrorRueckmeldungDto::class.java)
        val unmarshaller = context.createUnmarshaller()
        val dto =
            unmarshaller.unmarshal(StringReader(xmlContent!!)) as ErrorRueckmeldungDto

        dto.kopf.empfaenger shouldBe "SOZIALVERSICHERUNG"
        dto.status shouldBe "TECHNISCHER_FEHLER"
        dto.fehler.code shouldBe "TECH-500"
    }
})
