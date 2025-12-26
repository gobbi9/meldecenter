package coding.challenge.meldecenter

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<MeldecenterApplication>().with(TestcontainersConfiguration::class).run(*args)
}
