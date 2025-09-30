package com.leviyehonatan.tunity.data

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

@Serializable
data class ExposedUser(val username: String, val age: Int, val password: String)
class UserService(database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val username = varchar("name", length = 50)
        val age = integer("age")
        val password = varchar("password", 256)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }


    fun create(user: ExposedUser): Int = transaction {
        Users.insert {
            it[username] = user.username
            it[age] = user.age
            it[password] = user.password
        }[Users.id]
    }

    fun read(id: Int): ExposedUser? =
        Users.selectAll()
            .where { Users.id eq id }
            .map { ExposedUser(it[Users.username], it[Users.age], password = it[Users.password]) }
            .singleOrNull()


    fun update(id: Int, user: ExposedUser) {
        Users.update({ Users.id eq id }) {
            it[username] = user.username
            it[age] = user.age
            it[password] = user.password
        }
    }


    fun delete(id: Int) {
        Users.deleteWhere { Users.id.eq(id) }
    }
}