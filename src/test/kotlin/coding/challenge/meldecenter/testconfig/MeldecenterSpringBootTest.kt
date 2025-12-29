package coding.challenge.meldecenter.testconfig

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration::class)
@org.springframework.test.context.ContextConfiguration(initializers = [FtpInitializer::class])
annotation class MeldecenterSpringBootTest

class FtpInitializer : org.springframework.context.ApplicationContextInitializer<org.springframework.context.ConfigurableApplicationContext> {
    override fun initialize(applicationContext: org.springframework.context.ConfigurableApplicationContext) {
        TestcontainersConfiguration.ftpContainerInstance.start()
        org.springframework.test.context.support.TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
            applicationContext,
            "ftp.host=" + TestcontainersConfiguration.ftpContainerInstance.host,
            "ftp.port=" + TestcontainersConfiguration.ftpContainerInstance.getMappedPort(21)
        )
    }
}
