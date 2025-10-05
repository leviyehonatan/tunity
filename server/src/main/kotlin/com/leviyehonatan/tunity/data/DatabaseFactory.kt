package com.leviyehonatan.tunity.data

import org.jetbrains.exposed.v1.jdbc.Database
import io.ktor.server.application.Application
import io.ktor.server.config.property
import kotlinx.serialization.Serializable


@Serializable
data class DatabaseConfig(val url: String, val driver: String, val user: String = "", val password: String = "")

fun Application.createDatabase() : Database {
    val databaseConfig: DatabaseConfig = property("database")
    return Database.connect(
        databaseConfig.url,
        databaseConfig.driver,
        databaseConfig.user,
        databaseConfig.password
    )
}