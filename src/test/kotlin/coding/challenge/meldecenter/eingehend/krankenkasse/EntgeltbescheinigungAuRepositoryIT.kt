package coding.challenge.meldecenter.eingehend.krankenkasse

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportRepository
import coding.challenge.meldecenter.ausgehend.export.ExportStatus
import coding.challenge.meldecenter.ausgehend.krankenkasse.export.EntgeltbescheinigungAuExportRepository
import coding.challenge.meldecenter.eingehend.shared.MeldungTyp
import coding.challenge.meldecenter.shared.krankenkasse.EntgeltbescheinigungAuEntity
import coding.challenge.meldecenter.testconfig.MeldecenterSpringBootTest
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@MeldecenterSpringBootTest
class EntgeltbescheinigungAuRepositoryIT(
    private val repository: EntgeltbescheinigungAuRepository,
    private val exportRepository: ExportRepository,
    private val auExportRepository: EntgeltbescheinigungAuExportRepository,
) : StringSpec({
    extensions(SpringExtension)

    fun createEntity(id: UUID = UUID.randomUUID(), meldungId: UUID = UUID.randomUUID(), bn: String = "BN123", createdAt: LocalDateTime = LocalDateTime.now(), exportId: Long? = null) = EntgeltbescheinigungAuEntity(
        meldecenterId = id,
        meldungId = meldungId,
        meldungTyp = MeldungTyp.ENTGELTBESCHEINIGUNG_KG.toString(),
        meldungQuelle = "QUELLE",
        meldungMandantId = "MANDANT",
        meldungErstelltAm = LocalDateTime.now(),
        arbeitgeberBetriebsnummer = bn,
        mitarbeiterId = "EMP1",
        mitarbeiterVorname = "Max",
        mitarbeiterNachname = "Mustermann",
        mitarbeiterGeburtsdatum = LocalDate.of(1980, 1, 1),
        mitarbeiterSozialversicherungsnummer = "SV123",
        mitarbeiterAnschriftStrasse = "Strasse",
        mitarbeiterAnschriftPlz = "12345",
        mitarbeiterAnschriftOrt = "Ort",
        mitarbeiterKontaktEmail = "test@example.com",
        mitarbeiterKontaktTelefonnummer = "12345",
        mitarbeiterKontaktBevorzugteKontaktsart = "EMAIL",
        krankheitArbeitsunfaehigkeitBeginn = LocalDate.now(),
        krankheitArbeitsunfaehigkeitEnde = LocalDate.now(),
        entgeltBezugszeitraum = "2025-01",
        entgeltBruttoentgelt = BigDecimal.TEN,
        auditCreatedAt = createdAt,
        auditCreatedBy = "SYSTEM",
        exportId = exportId
    )

    "Should read test migration data" {
        val bescheinigungen = repository.findAll().toList()
        bescheinigungen.size shouldBeGreaterThanOrEqual 2
    }

    "Should save and find new entity" {
        val id = UUID.randomUUID()
        val entity = createEntity(id = id)
        repository.save(entity)
        val saved = repository.findById(id)

        saved.shouldNotBeNull()
        saved.meldecenterId shouldBe id
    }

    "Should update an entity" {
        val id = UUID.randomUUID()
        val entity = createEntity(id = id)
        val saved = repository.save(entity)

        val now = LocalDateTime.now()
        saved.auditUpdatedAt = now
        repository.save(saved)

        val updated = repository.findById(id)
        updated.shouldNotBeNull()
        updated.auditUpdatedAt shouldBe now
    }

    "Should find all export groups" {
        val bn = "GRP_" + UUID.randomUUID().toString().take(8)
        repository.save(createEntity(bn = bn))

        val groups = auExportRepository.findAllExportGroups().toList()
        groups.any { it.betriebsnummer == bn } shouldBe true
    }

    "Should deduplicate entries" {
        val bn = "DEDUPE_" + UUID.randomUUID().toString().take(8)
        val meldungId = UUID.randomUUID()
        val e1 = createEntity(meldungId = meldungId, bn = bn, createdAt = LocalDateTime.now().minusDays(1))
        val e2 = createEntity(meldungId = meldungId, bn = bn, createdAt = LocalDateTime.now())

        repository.save(e1)
        repository.save(e2)

        val duplicateCount = auExportRepository.deduplicate(bn)
        duplicateCount shouldBe 1

        val entries = repository.findAll().toList()
        val deduplicated = entries.filter { it.exportId == 1L && it.arbeitgeberBetriebsnummer == bn }
        deduplicated.size shouldBe 1
    }

    "Should assign to export" {
        val bn = "ASSIGN_" + UUID.randomUUID().toString().take(8)
        val entity = createEntity(bn = bn)
        repository.save(entity)

        val export = exportRepository.save(ExportEntity(
            typ = MeldungTyp.ENTGELTBESCHEINIGUNG_KG.toString(),
            status = ExportStatus.CREATED,
            betriebsnummer = bn,
            createdBy = "test"
        ))
        val exportId = export.id
        val assignedCount = auExportRepository.assignToExport(bn, exportId)
        assignedCount shouldBe 1

        val entries = auExportRepository.findByExportId(exportId).toList()
        entries.size shouldBe 1
        entries.all { it.arbeitgeberBetriebsnummer == bn } shouldBe true
    }
})
