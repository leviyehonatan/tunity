package com.leviyehonatan.tunity.plugins

import com.leviyehonatan.tunity.LoginRequest
import com.leviyehonatan.tunity.RegisterRequest
import com.leviyehonatan.tunity.data.UserService
import com.leviyehonatan.tunity.data.createDatabase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.configureDatabases(database: Database = createDatabase()) {
}