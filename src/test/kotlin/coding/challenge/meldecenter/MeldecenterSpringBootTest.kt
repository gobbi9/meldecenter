package coding.challenge.meldecenter

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@SpringBootTest
@Import(TestcontainersConfiguration::class)
annotation class MeldecenterSpringBootTest
