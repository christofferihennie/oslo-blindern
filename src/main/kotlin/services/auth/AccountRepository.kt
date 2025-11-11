package dev.hennie.services.auth

import org.koin.core.annotation.Single
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

@Single
class AccountRepository {
    private val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

    fun encodePassword(password: String): String = encoder.encode(password)

    fun verifyPassword(password: String, hash: String): Boolean = encoder.matches(password, hash)
}