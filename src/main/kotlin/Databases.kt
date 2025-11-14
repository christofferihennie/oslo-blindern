package dev.hennie

import dev.hennie.services.auth.AccountTable
import dev.hennie.services.users.UserTable
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases(config: ApplicationConfig) {
    val url = config.property("storage.url").getString()
    val driver = config.property("storage.driver").getString()
    val user = config.property("storage.user").getString()
    val password = config.property("storage.password").getString()

    val database = Database.connect(url, driver, user, password)

    monitor.subscribe(ApplicationStarted) {
        println("Creating tables")
        transaction {
            exec("CREATE SCHEMA IF NOT EXISTS oslo")
            SchemaUtils.create(AccountTable)
            SchemaUtils.create(UserTable)
        }
        println("Tables created")
    }

}
