package coding.challenge.meldecenter.ausgehend.sozialversicherung.export

import coding.challenge.meldecenter.ausgehend.export.ExportEntity
import coding.challenge.meldecenter.ausgehend.export.ExportPreparer
import coding.challenge.meldecenter.ausgehend.export.ExportService
import coding.challenge.meldecenter.ausgehend.export.toXmlFilename
import coding.challenge.meldecenter.ausgehend.ftp.XmlFileUploader
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.inbox.DeuevAnmeldungenDto
import coding.challenge.meldecenter.ausgehend.sozialversicherung.ftp.model.newDeuevAnmeldungenXmlDto
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

/**
 * Service zum Hochladen von DEÜV-Anmeldungen auf den FTP-Server.
 *
 * Bereitet den Export vor, generiert das XML-DTO und führt den Upload durch.
 *
 * @property exportPreparer Der Preparer zur Vorbereitung des Exports (Status-Aktualisierung).
 * @property repository Das Repository für DEÜV-Anmeldungen.
 * @property xmlFileUploader Der generische Uploader für XML-Dateien.
 * @property exportService Der Service zur Verwaltung von Exporten.
 */
@Service
class DeuevAnmeldungExportUploader(
    private val exportPreparer: ExportPreparer,
    private val repository: DeuevAnmeldungExportRepository,
    private val xmlFileUploader: XmlFileUploader<DeuevAnmeldungenDto>,
    private val exportService: ExportService,
) {
    /**
     * Bereitet den Export vor, serialisiert die Meldungen als XML und lädt sie hoch.
     *
     * @param unpreparedExport Die noch nicht vorbereitete Export-Entität.
     * @return Die aktualisierte [ExportEntity] nach dem Upload oder `null`, falls die Vorbereitung fehlschlug.
     */
    @NewSpan
    suspend fun upload(unpreparedExport: ExportEntity): ExportEntity? {
        val export = exportPreparer.prepare(unpreparedExport) ?: return null
        val meldungen = repository.findByExportId(export.id)
        val meldungXmlDto =
            newDeuevAnmeldungenXmlDto(export, meldungen.toList())
        val xmlFilename = export.toXmlFilename()

        xmlFileUploader.upload(
            xmlDto = meldungXmlDto,
            xmlFilename = xmlFilename,
            exportId = export.id
        )
        return exportService.findById(export.id)
    }
}
