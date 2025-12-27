package coding.challenge.meldecenter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MeldecenterApplication

fun main(args: Array<String>) {
    runApplication<MeldecenterApplication>(*args)
}

// Kotlin map type DEUEV_ANMELDUNG to a generic content, maybe delegation or generics?
// JUST INTERFACE WITH "request" as property, AND REDIRECT TO CORRECT SERVICE
// or SEALED CLASS that wraps all these properties and implements an interface with request object
/*

For the DEÜV-Anmeldung.json a good deduplication strategy would be to use a combination of
- request.type (probably not needed, since each type has its own table)
- mitarbeiter_id
- beschaeftigung_beginn
then the message with the newest request.createdAt timestamp should be the winner.
Use PostgreSQL PARTITION BY

For Entgeltbescheinigung-Arbeitsunfähigkeit.json
- mitarbeiter_id
- krankheit_arbeitsunfaehigkeit_beginn
- krankheit_arbeitsunfaehigkeit_ende
 */
