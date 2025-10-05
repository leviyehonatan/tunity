package com.leviyehonatan.tunity.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.leviyehonatan.tunity.LoginRequest
import com.leviyehonatan.tunity.RegisterRequest
import com.leviyehonatan.tunity.data.UserService
import com.leviyehonatan.tunity.data.createDatabase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.config.property
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.authRoutes() {
    val db = createDatabase()
    val userService = UserService(db)
    routing {
//        // Create user
//        post("/users") {
//            val user = call.receive<NewUserRegistration>()
//            val id = userService.create(ExposedUser(user.username, age = 0, password = user.password))
//            call.respond(HttpStatusCode.Created, id)
//        }
//        // Read user
//        get("/users/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            val user = userService.read(id)
//            if (user != null) {
//                call.respond(HttpStatusCode.OK, user)
//            } else {
//                call.respond(HttpStatusCode.NotFound)
//            }
//        }
//        // Update user
//        put("/users/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            val user = call.receive<ExposedUser>()
//            userService.update(id, user)
//            call.respond(HttpStatusCode.OK)
//        }
//        // Delete user
//        delete("/users/{id}") {
//            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
//            userService.delete(id)
//            call.respond(HttpStatusCode.OK)
//        }

        post("/register") {
            val body = call.receive<RegisterRequest>()
            if (userService.register(body.username, body.password)) {
                    call.respond(HttpStatusCode.OK, "User registered successfully")
            } else {
                call.respond(HttpStatusCode.Conflict, "User Exists")
            }
        }

        post("/login") {
            val body = call.receive<LoginRequest>()
            if (userService.login(body.username, body.password)) {
                    val jwtConfig: JwtConfig = property("jwt")
                val jwtToken = JWT.create()
                    .withClaim("username",body.username )
                    .sign(Algorithm.HMAC256(jwtConfig.secret) )
                call.respond(HttpStatusCode.OK, hashMapOf("token" to jwtToken))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")

            }
        }

        authenticate("auth-jwt") {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                print("Hello, $username! Token is expired at $expiresAt ms.")
                call.respond(HttpStatusCode.OK, "Authenticated")
            }
        }

    }
}