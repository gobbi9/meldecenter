package coding.challenge.meldecenter.ausgehend.krankenkasse.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportPreparer
import coding.challenge.meldecenter.ausgehend.export.ExportService
import coding.challenge.meldecenter.ausgehend.export.toXmlFilename
import coding.challenge.meldecenter.ausgehend.ftp.XmlFileUploader
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.inbox.EntgeltbescheinigungenAuDto
import coding.challenge.meldecenter.ausgehend.krankenkasse.ftp.model.newEntgeltbescheinigungenAuXmlDto
import coding.challenge.meldecenter.eingehend.krankenkasse.EntgeltbescheinigungAuRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 *  Lädt Entgeltbescheinigungen zu dem FTP Server hoch.
 */
@Service
class EntgeltbescheinigungenAuExportUploader(
    private val exportPreparer: ExportPreparer,
    private val repository: EntgeltbescheinigungAuRepository,
    private val xmlFileUploader: XmlFileUploader<EntgeltbescheinigungenAuDto>,
    private val exportService: ExportService,
) {
    @NewSpan
    suspend fun upload(unpreparedExport: ExportEntity): ExportEntity? {
        val export = exportPreparer.prepare(unpreparedExport) ?: return null
        val meldungen = repository.findByExportId(export.id)
        val meldungXmlDto =
            newEntgeltbescheinigungenAuXmlDto(export, meldungen.toList())
        val xmlFilename = export.toXmlFilename()

        xmlFileUploader.upload(
            xmlDto = meldungXmlDto,
            xmlFilename = xmlFilename,
            exportId = export.id
        )
        return exportService.findById(export.id)
    }
}
