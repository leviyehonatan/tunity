package com.leviyehonatan.tunity.auth

import com.leviyehonatan.tunity.RegisterRequest
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

fun findUserByUsername(username: String): UserEntity? =
    transaction {
        UserEntity.find { UsersTable.username eq username }.singleOrNull()
    }


fun register(registerRequest: RegisterRequest): Boolean =
    transaction {
        if (UserEntity.find { UsersTable.username eq registerRequest.username }.count() > 0) {
            false
        } else {
            val hashed = BCrypt.hashpw(registerRequest.password, BCrypt.gensalt())
            UserEntity.new {
                username = registerRequest.username
                hashedPassword = hashed
            }
            true
        }
    }

fun login(username: String, password: String) =
    transaction {
        UserEntity.find { UsersTable.username eq username }
            .map { BCrypt.checkpw(password, it.hashedPassword) }
            .singleOrNull() ?: false
    }
