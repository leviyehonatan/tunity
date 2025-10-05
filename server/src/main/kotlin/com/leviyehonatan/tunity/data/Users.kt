package com.leviyehonatan.tunity.data

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class UserService(database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val username = varchar("username", length = 50)
        val hashedPassword = varchar("hashedPassword", 256)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }


    fun register(username: String, password: String): Boolean =
        transaction {
            if (Users.selectAll().where { Users.username eq username }.count() > 0) {
                false
            } else {
                val hashed = BCrypt.hashpw(password, BCrypt.gensalt())
                Users.insert {
                    it[Users.username] = username
                    it[Users.hashedPassword] = hashed
                }
                true
            }
        }



    fun login(username: String, password: String) =
        transaction {
            Users.selectAll().where { Users.username eq username }
                .map { BCrypt.checkpw(password, it[Users.hashedPassword]) }
                .singleOrNull() ?: false
        }

}