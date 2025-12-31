package coding.challenge.meldecenter.ausgehend.krankenkasse.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportRepository
import coding.challenge.meldecenter.ausgehend.export.ExportStatus
import coding.challenge.meldecenter.ausgehend.export.toXmlFilename
import coding.challenge.meldecenter.ausgehend.ftp.FtpService
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.KgBescheinigungDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.newEntgeltbescheinigungenAuXmlDto
import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltbescheinigungAuRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import jakarta.xml.bind.JAXBContext
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.name

private val log = KotlinLogging.logger {}

/**
 *  Lädt Entgeltbescheinigungen zu dem FTP Server hoch.
 */
@Service
class EntgeltbescheinigungenAuExportUploader(
    private val entgeltbescheinigungAuRepository: EntgeltbescheinigungAuRepository,
    private val exportRepository: ExportRepository,
    private val ftpService: FtpService,
) {
    private val marshaller = JAXBContext
        .newInstance(KgBescheinigungDto::class.java)
        .createMarshaller()

    @NewSpan
    suspend fun upload(export: ExportEntity): ExportEntity? {
        val meldungen =
            entgeltbescheinigungAuRepository.findByExportId(export.id)
        val meldungXmlDto =
            newEntgeltbescheinigungenAuXmlDto(export, meldungen.toList())
        val xmlFilename = export.toXmlFilename()

        var tempFile: Path? = null
        try {
            tempFile = createTempFile(
                prefix = "export-${export.id}-",
                suffix = ".xml"
            )
            marshaller.marshal(meldungXmlDto, tempFile.toFile())

            ftpService.uploadFile(
                localFilePath = tempFile.toString(),
                remoteFileName = xmlFilename
            )
            updateStatus(exportId = export.id, status = ExportStatus.EXPORTED)
        } catch (e: Exception) {
            log.error(e) { "Fehler beim Hochladen von: $xmlFilename " }
            updateStatus(exportId = export.id, status = ExportStatus.FAILED)
        } finally {
            tempFile?.let { cleanTemporaryFile(it) }
        }
        return exportRepository.findById(export.id)
    }

    private suspend fun updateStatus(exportId: Long, status: ExportStatus) {
        log.debug { "Aktualisiere Export Status: $exportId -> $status" }
        val updateCount = exportRepository.updateStatus(
            id = exportId,
            newStatus = status,
            currentStatus = ExportStatus.EXPORTING
        )
        if (updateCount > 0) {
            log.debug { "Aktualisiert Export Status: $exportId -> $status" }
        }
    }

    private fun cleanTemporaryFile(path: Path) {
        try {
            val deleted = path.deleteIfExists()
            log.debug { "Temporäre Datei: ${path.name} gelöscht?: $deleted" }
        } catch (e: Exception) {
            log.error { "Temporäre Datei: ${path.name} konnte nicht gelöscht werden: ${e.message}" }
        }
    }
}
