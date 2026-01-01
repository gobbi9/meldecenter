package coding.challenge.meldecenter.ausgehend.ftp

import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.annotation.NewSpan
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.File

private val log = KotlinLogging.logger {}

/**
 * Service for performing FTP operations such as uploading, reading, and deleting files.
 *
 * @property sessionFactory The factory used to create FTP sessions.
 */
@Service
class FtpService(private val sessionFactory: DefaultFtpSessionFactory) {

    /**
     * Uploads a local file to the FTP server.
     *
     * @param localFilePath The path to the local file to be uploaded.
     * @param remoteFileName The name of the file on the FTP server.
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
     * Reads a file from the FTP server and returns its content as a string.
     *
     * @param remoteFileName The name of the file to be read from the FTP server.
     * @return The content of the file as a string.
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
     * Deletes a file from the FTP server.
     *
     * @param remoteFileName The name of the file to be deleted from the FTP server.
     * @return True if the file was successfully deleted, false otherwise.
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
