package coding.challenge.meldecenter.ausgehend.export.event

import coding.challenge.meldecenter.ausgehend.export.ExportEventType
import coding.challenge.meldecenter.ausgehend.export.ExportRepository
import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@MeldecenterSpringBootTest
class ExportEventRepositoryIT(
    private val repository: ExportEventRepository,
    private val exportRepository: ExportRepository,
) : StringSpec({

    "Should save and find ExportEventEntity" {
        // Given an export
        val export = exportRepository.findById(1L)
        export.shouldNotBeNull()

        // When saving an event
        val event = ExportEventEntity(
            exportId = export.id,
            type = ExportEventType.START_EXPORT,
            details = "Export started for testing",
            createdBy = "test-user"
        )
        val savedEvent = repository.save(event)
        savedEvent.id.shouldNotBeNull()
        savedEvent.version shouldBe 0

        // Then it can be found
        val foundEvent = repository.findById(savedEvent.id)
        foundEvent.shouldNotBeNull()
        foundEvent.exportId shouldBe export.id
        foundEvent.type shouldBe ExportEventType.START_EXPORT
        foundEvent.details shouldBe "Export started for testing"
        foundEvent.createdBy shouldBe "test-user"
    }

    "Should update ExportEventEntity" {
        // Given a saved event
        val event = ExportEventEntity(
            exportId = 1L,
            type = ExportEventType.START_EXPORT,
            createdBy = "test-user"
        )
        val savedEvent = repository.save(event)

        // When updating details
        val updatedEvent = savedEvent.copy(details = "Updated details")
        repository.save(updatedEvent)

        // Then version is incremented and details are updated
        val foundEvent = repository.findById(savedEvent.id)
        foundEvent.shouldNotBeNull()
        foundEvent.details shouldBe "Updated details"
        foundEvent.version shouldBe 1
    }
})
