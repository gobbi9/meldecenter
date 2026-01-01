package coding.challenge.meldecenter.ausgehend.ftp

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory

private val log = KotlinLogging.logger {}

/**
 * Konfigurationsklasse für die FTP-Client-Einstellungen und Bean-Definitionen.
 *
 * @property host Der Hostname des FTP-Servers (Standard: localhost).
 * @property port Der Port des FTP-Servers (Standard: 21).
 */
@Configuration
class FtpClientConfig(
    @param:Value("\${ftp.host}") val host: String,
    @param:Value("\${ftp.port}") val port: Int,
) {

    /**
     * Erstellt und konfiguriert eine [DefaultFtpSessionFactory].
     *
     * Die Session-Factory wird mit Host, Port, Benutzername und Passwort konfiguriert
     * und verwendet den passiven Modus.
     *
     * @return Eine konfigurierte Instanz von [DefaultFtpSessionFactory].
     */
    @Bean
    fun sessionFactory(): DefaultFtpSessionFactory {
        log.debug { "Creating DefaultFtpSessionFactory for host: $host, port: $port" }
        val sf = DefaultFtpSessionFactory()
        sf.setHost(host)
        sf.setPort(port)
        sf.setUsername("admin")
        sf.setPassword("admin")
        sf.setClientMode(2) // PASSIVE_LOCAL_DATA_CONNECTION_MODE
        return sf
    }
}
