package dev.hennie.services.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.JWTAuthenticationProvider
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.config.ApplicationConfig
import java.util.Date

object JwtConfig {
    private lateinit var privateKeyString: String
    private lateinit var issuer: String
    private lateinit var audience: String
    private lateinit var myRealm: String
    private lateinit var algorithm: Algorithm

    fun init(config: ApplicationConfig) {
        privateKeyString = config.property("ktor.jwt.privateKey").getString()
        issuer = config.property("ktor.jwt.issuer").getString()
        audience = config.property("ktor.jwt.audience").getString()
        myRealm = config.property("ktor.jwt.realm").getString()

        algorithm = Algorithm.HMAC256(privateKeyString)
    }

    fun makeAccessToken(userId: String): String =
        JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(algorithm)

    fun makeRefreshToken(userId: String): String =
        JWT.create()
            .withSubject("Refresh")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withExpiresAt(java.util.Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
            .sign(algorithm)

    fun configureJwt(config: JWTAuthenticationProvider.Config) = with(config) {
        verifier(JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build())
        validate { credential ->
            if (credential.payload.getClaim("userId").asString().isNotEmpty())
                JWTPrincipal(credential.payload)
            else
                null
        }
    }

    fun getAlgorithm(): Algorithm = algorithm
}
