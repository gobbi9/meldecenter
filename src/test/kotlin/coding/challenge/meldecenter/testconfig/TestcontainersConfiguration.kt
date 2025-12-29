package coding.challenge.meldecenter.testconfig

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> {
        return PostgreSQLContainer(DockerImageName.parse("postgres:17.7-alpine"))
    }

    @Bean
    fun ftpContainer(): GenericContainer<*> {
        return ftpContainerInstance
    }

    companion object {
        val ftpContainerInstance: FixedHostPortGenericContainer<*> =
        FixedHostPortGenericContainer("delfer/alpine-ftp-server:latest")
            .withEnv("USERS", "admin|admin")
            .withFixedExposedPort(21, 21)
            .withFixedExposedPort(21000, 21000)
            .withEnv("MIN_PORT", "21000")
            .withEnv("MAX_PORT", "21000")
            .withFileSystemBind("src/test/resources/ausgehend/ftp", "/ftp/admin")
            .waitingFor(Wait.forListeningPort())
    }
}
