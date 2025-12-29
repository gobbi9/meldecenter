package coding.challenge.meldecenter.ausgehend.ftp

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory

private val log = KotlinLogging.logger {}

/**
 * Configuration class for FTP client settings and bean definitions.
 *
 * @property host The FTP server host, defaults to localhost.
 * @property port The FTP server port, defaults to 21.
 */
@Configuration
class FtpClientConfig(
    @param:Value("\${ftp.host}") val host: String,
    @param:Value("\${ftp.port}") val port: Int,
) {

    /**
     * Creates and configures a [DefaultFtpSessionFactory].
     *
     * @return A configured [DefaultFtpSessionFactory] instance.
     */
    @Bean
    fun ftpSessionFactory(): DefaultFtpSessionFactory {
        log.debug { "Creating DefaultFtpSessionFactory for host: $host, port: $port" }
        val sf = DefaultFtpSessionFactory()
        sf.setHost(host)
        sf.setPort(port)
        sf.setUsername("admin")
        sf.setPassword("admin")
        sf.setClientMode(2) // PASSIVE_LOCAL_DATA_CONNECTION_MODE
        return sf
    }

    /**
     * Creates a [FtpService] bean.
     *
     * @param sessionFactory The [DefaultFtpSessionFactory] to be used by the service.
     * @return An instance of [FtpService].
     */
    @Bean
    fun ftpService(sessionFactory: DefaultFtpSessionFactory): FtpService {
        log.debug { "Creating FtpService bean" }
        return FtpService(sessionFactory)
    }
}
