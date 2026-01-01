package coding.challenge.meldecenter.eingehend.eap

import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltbescheinigungAuController
import coding.challenge.meldecenter.eingehend.shared.meldungen.DeuevAnmeldungDto
import coding.challenge.meldecenter.eingehend.shared.meldungen.EntgeltbescheinigungAuDto
import coding.challenge.meldecenter.eingehend.sozialversicherung.DeuevAnmeldungController
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class MeldungGatewayControllerTest : StringSpec({

    val deuevController = mockk<DeuevAnmeldungController>()
    val entgeltController = mockk<EntgeltbescheinigungAuController>()
    val gatewayController = MeldungGatewayController(deuevController, entgeltController)

    "Save should delegate to DeuevAnmeldungController for DeuevAnmeldungDto" {
        val dto = mockk<DeuevAnmeldungDto>()
        coEvery { deuevController.save(dto) } returns dto

        val result = gatewayController.save(dto)

        result shouldBe dto
    }

    "Save should delegate to EntgeltbescheinigungAuController for EntgeltbescheinigungAuDto" {
        val dto = mockk<EntgeltbescheinigungAuDto>()
        coEvery { entgeltController.save(dto) } returns dto

        val result = gatewayController.save(dto)

        result shouldBe dto
    }
})
