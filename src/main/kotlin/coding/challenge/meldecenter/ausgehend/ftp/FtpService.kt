package coding.challenge.meldecenter.ausgehend.ftp

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.File

private val log = KotlinLogging.logger {}

/**
 * Service für die Durchführung von FTP-Operationen wie Hochladen, Lesen und Löschen von Dateien.
 *
 * @property sessionFactory Die Factory zur Erstellung von FTP-Sessions.
 */
@Service
class FtpService(private val sessionFactory: DefaultFtpSessionFactory) {

    /**
     * Lädt eine lokale Datei auf den FTP-Server hoch.
     *
     * @param localFilePath Der Pfad zur lokalen Datei, die hochgeladen werden soll.
     * @param remoteFileName Der Ziel-Dateiname auf dem FTP-Server.
     */
    @NewSpan
    fun uploadFile(localFilePath: String, remoteFileName: String) {
        log.debug { "Uploading file: $localFilePath to $remoteFileName" }
        sessionFactory.session.use { session ->
            val inputStream = File(localFilePath).inputStream()
            session.write(inputStream, remoteFileName)
        }
        log.debug { "Successfully uploaded file: $remoteFileName" }
    }

    /**
     * Liest eine Datei vom FTP-Server und gibt ihren Inhalt als String zurück.
     *
     * @param remoteFileName Der Name der Datei auf dem FTP-Server.
     * @return Der Inhalt der Datei als String.
     */
    @NewSpan
    fun readFile(remoteFileName: String): String {
        log.debug { "Reading file from FTP: $remoteFileName" }
        val content = sessionFactory.session.use { session ->
            val outputStream = ByteArrayOutputStream()
            session.read(remoteFileName, outputStream)
            outputStream.toString()
        }
        log.debug { "Successfully read file: $remoteFileName (length: ${content.length})" }
        return content
    }

    /**
     * Löscht eine Datei vom FTP-Server.
     *
     * @param remoteFileName Der Name der zu löschenden Datei auf dem FTP-Server.
     * @return `true`, wenn die Datei erfolgreich gelöscht wurde, andernfalls `false`.
     */
    @NewSpan
    fun deleteFile(remoteFileName: String): Boolean {
        log.debug { "Deleting file from FTP: $remoteFileName" }
        val result = sessionFactory.session.use { session ->
            session.remove(remoteFileName)
        }
        log.debug { "Delete result for $remoteFileName: $result" }
        return result
    }
}
