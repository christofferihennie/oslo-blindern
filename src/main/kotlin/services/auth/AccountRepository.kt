package dev.hennie.services.auth

import dev.hennie.services.auth.AccountTable.aid
import dev.hennie.services.auth.AccountTable.createdAt
import dev.hennie.services.auth.AccountTable.passwordHash
import dev.hennie.services.auth.AccountTable.uid
import dev.hennie.services.auth.AccountTable.updatedAt
import dev.hennie.services.auth.AccountTable.verified
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Single
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

data class AccountInfo(val aid: Int, val uid: Int, val verified: Boolean, val createdAt: Instant, val updatedAt: Instant)

@Single
class AccountRepository {
    private val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

    fun encodePassword(password: String): String = encoder.encode(password)
    fun verifyPassword(password: String, hash: String): Boolean = encoder.matches(password, hash)

    fun verifyAccount(password: String, userId: Int): Boolean = transaction {
        val hashedPassword = AccountTable.select(passwordHash).where {
            uid eq userId
        }.withDistinct().map { it[passwordHash] }.firstOrNull()

        if (hashedPassword.isNullOrEmpty()) return@transaction false

        return@transaction encoder.matches(password, hashedPassword)
    }

    fun createAccount(userId: Int, password: String): Instant = transaction {
        val hashedPassword = encodePassword(password)
        val createdUserTimestamp = AccountTable.insert {
            it[uid] = userId
            it[passwordHash] = hashedPassword
        } get createdAt

        return@transaction createdUserTimestamp
    }

    fun selectUsersAccount(userId: Int): List<AccountInfo> = transaction {
        return@transaction AccountTable.selectAll()
            .where { uid eq userId }
            .map {
                AccountInfo(
                    aid = it[aid],
                    uid = it[uid],
                    verified = it[verified],
                    createdAt = it[createdAt],
                    updatedAt = it[updatedAt]
                )
            }
    }
}