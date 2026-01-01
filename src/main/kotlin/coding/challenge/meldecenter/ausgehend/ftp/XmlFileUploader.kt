package coding.challenge.meldecenter.ausgehend.ftp

import coding.challenge.meldecenter.ausgehend.export.ExportService
import coding.challenge.meldecenter.ausgehend.export.ExportStatus
import coding.challenge.meldecenter.ausgehend.export.event.ExportEventService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Marshaller
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.name
import kotlin.io.path.readText

private val log = KotlinLogging.logger {}

@Service
class XmlFileUploader<T>(
    private val ftpService: FtpService,
    private val exportService: ExportService,
    private val exportEventService: ExportEventService,
) {
    private val contextCache = ConcurrentHashMap<Class<*>, JAXBContext>()

    @NewSpan
    suspend fun upload(xmlDto: T, xmlFilename: String, exportId: Long) {
        log.debug { "Hochladen von: $xmlFilename, exportId: $exportId" }
        log.trace { "XML-DTO: $xmlDto" }
        var tempFile: Path? = null
        try {
            tempFile = createTempFile(suffix = "-$xmlFilename")

            val context = contextCache.computeIfAbsent(xmlDto!!::class.java) {
                JAXBContext.newInstance(it)
            }
            val marshaller = context.createMarshaller()
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)

            marshaller.marshal(xmlDto, tempFile.toFile())
            log.trace { "XML-Datei: \n${tempFile.readText()}" }

            ftpService.uploadFile(
                localFilePath = tempFile.toAbsolutePath().toString(),
                remoteFileName = xmlFilename
            )
            exportService.updateStatus(exportId = exportId, status = ExportStatus.EXPORTED)
            exportEventService.saveEndEvent(exportId)
        } catch (e: Exception) {
            log.error(e) { "Fehler beim Hochladen von: $xmlFilename" }
            exportService.updateStatus(exportId = exportId, status = ExportStatus.FAILED)
            exportEventService.saveErrorEvent(exportId = exportId, error = e)
        } finally {
            tempFile?.let { cleanTemporaryFile(it) }
        }
    }

    private fun cleanTemporaryFile(path: Path) {
        try {
            val deleted = path.deleteIfExists()
            log.debug { "Temporäre Datei: ${path.name} gelöscht?: $deleted" }
        } catch (e: Exception) {
            log.warn { "Temporäre Datei: ${path.name} konnte nicht gelöscht werden: ${e.message}" }
        }
    }
}

