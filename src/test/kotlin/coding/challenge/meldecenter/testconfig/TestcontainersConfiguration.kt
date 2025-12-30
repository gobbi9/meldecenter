package coding.challenge.meldecenter.testconfig

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    fun postgresContainer(): PostgreSQLContainer<*> =
        PostgreSQLContainer(DockerImageName.parse("postgres:17.7-alpine"))

    @Bean
    fun ftpContainer(): FixedHostPortGenericContainer<*> =
        FixedHostPortGenericContainer("delfer/alpine-ftp-server:latest")
            .withEnv("USERS", "admin|admin")
            .withEnv("MIN_PORT", "21000")
            .withEnv("MAX_PORT", "21000")
            .withFixedExposedPort(21, 21)
            .withFixedExposedPort(21000, 21000)
            .withCopyToContainer(
                MountableFile.forHostPath("src/test/resources/ausgehend/ftp"),
                "/ftp/admin"
            )
            .waitingFor(Wait.forListeningPort())
}
