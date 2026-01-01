package coding.challenge.meldecenter.ausgehend.export

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import org.springframework.test.web.reactive.server.WebTestClient

@MeldecenterSpringBootTest
class ExportControllerIT(
    private val webTestClient: WebTestClient
) : StringSpec({

    "POST /v1/meldung/export should propagate traceId to background logs" {
        val logger = LoggerFactory.getLogger("coding.challenge.meldecenter") as Logger
        val listAppender = ListAppender<ILoggingEvent>()
        listAppender.start()
        logger.addAppender(listAppender)

        try {
            webTestClient.post()
                .uri("/v1/meldung/export")
                .exchange()
                .expectStatus().isAccepted

            // Wait a bit for background task to complete or at least log something
            var foundStart = false
            var foundEnd = false
            var foundGroup = false
            var traceId: String? = null

            for (i in 1..100) {
                val logs = listAppender.list.toList() // Copy to avoid concurrent modification
                val startLog = logs.find { it.message.contains("POST /v1/meldung/export aufgerufen") }
                if (startLog != null) {
                    foundStart = true
                    traceId = startLog.mdcPropertyMap["traceId"]
                }

                val groupLog = logs.find { it.message.contains("Entgeltbescheinigungen Gruppe:") }
                if (groupLog != null) {
                    foundGroup = true
                    if (traceId != null) {
                        groupLog.mdcPropertyMap["traceId"] shouldBe traceId
                    }
                }

                val endLog = logs.find { it.message.contains("Export abgeschlossen") }
                if (endLog != null) {
                    foundEnd = true
                    if (traceId != null) {
                        endLog.mdcPropertyMap["traceId"] shouldBe traceId
                    }
                }

                if (foundStart && foundEnd && foundGroup) break
                delay(100)
            }

            foundStart shouldBe true
            traceId.shouldNotBeBlank()
            foundGroup shouldBe true
            foundEnd shouldBe true

        } finally {
            logger.detachAppender(listAppender)
            listAppender.stop()
        }
    }
})
