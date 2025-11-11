package services.auth

import dev.hennie.infrastructure.AppModule
import dev.hennie.services.auth.AccountRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension

class AccountRepositoryTest: KoinTest {
    private val accountRepository by inject<AccountRepository>()

    private val inputPassword = "test"

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules( AppModule().module )
    }

    @Test
    fun encodePassword() {
        val hashedPassword = accountRepository.encodePassword(inputPassword)

        println(hashedPassword)
        assertNotNull(hashedPassword)
    }

    @Test
    fun verifyPassword() {
        val hashedPassword = accountRepository.encodePassword(inputPassword)
        val verifiedPassword = accountRepository.verifyPassword(
            inputPassword,
            hashedPassword
        )

        assertTrue(verifiedPassword)
    }

}