package com.leviyehonatan.tunity.auth

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object UsersTable : IntIdTable() {
    val username = varchar("username", length = 50)
    val hashedPassword = varchar("hashedPassword", 256)
}