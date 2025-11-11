package dev.hennie

import dev.hennie.services.auth.JwtConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    JwtConfig.init(environment.config)

    authentication {
        jwt {
            JwtConfig.configureJwt(this)
        }
    }
}
