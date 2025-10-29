package com.leviyehonatan.tunity.data

import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.Database


@Serializable
data class DatabaseConfig(val url: String, val driver: String, val user: String = "", val password: String = "")

fun Application.createDatabase(): Database {
    val databaseConfig: DatabaseConfig = property("database")
    return Database.connect(
        databaseConfig.url,
        databaseConfig.driver,
        databaseConfig.user,
        databaseConfig.password
    )
}
