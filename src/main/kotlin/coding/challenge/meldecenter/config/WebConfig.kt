package coding.challenge.meldecenter.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer

/**
 * Ermöglicht die Verwendung von Pageable-Argumenten in Spring WebFlux-Controller-Methoden.
 */
@Configuration
class WebConfig : WebFluxConfigurer {
    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        configurer.addCustomResolver(
            ReactivePageableHandlerMethodArgumentResolver()
        )
    }
}
