package com.leviyehonatan.tunity.plugins

import com.leviyehonatan.tunity.NewUserRegistration
import com.leviyehonatan.tunity.data.ExposedUser
import com.leviyehonatan.tunity.data.UserService
import com.leviyehonatan.tunity.data.createDatabase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.configureDatabases(database: Database = createDatabase()) {
    val userService = UserService(database)
    routing {
        // Create user
        post("/users") {
            val user = call.receive<NewUserRegistration>()
            val id = userService.create(ExposedUser(user.username, age = 0, password = user.password))
            call.respond(HttpStatusCode.Created, id)
        }
        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update user
        put("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<ExposedUser>()
            userService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }
        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}