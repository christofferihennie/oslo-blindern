package dev.hennie.services.auth

import dev.hennie.services.users.UserTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object AccountTable : Table("oslo.accounts") {
    val aid = integer("account_id").autoIncrement()
    val uid = reference("user_id", UserTable.uid, ReferenceOption.CASCADE)
    val passwordHash = varchar("password_hash", 255)
    val verified = bool("verified").default(false)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(aid, uid)
}
