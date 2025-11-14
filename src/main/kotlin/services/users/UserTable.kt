package dev.hennie.services.users

import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object UserTable : Table("oslo.users") {
    val uid = integer("uid").autoIncrement()
    val firstName = text("first_name")
    val lastName = text("last_name")
    val email = text("email")
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(uid)
}
