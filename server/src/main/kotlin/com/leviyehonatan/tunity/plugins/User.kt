package com.leviyehonatan.tunity.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.leviyehonatan.tunity.CreateTagRequest
import com.leviyehonatan.tunity.CreateTuneRequest
import com.leviyehonatan.tunity.LoginRequest
import com.leviyehonatan.tunity.RegisterRequest
import com.leviyehonatan.tunity.Translation
import com.leviyehonatan.tunity.TuneTag
import com.leviyehonatan.tunity.data.DatabaseService
import com.leviyehonatan.tunity.data.TagEntity
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.config.property
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.authRoutes() {
    val databaseService = DatabaseService()
    routing {
        post("/register") {
            val body = call.receive<RegisterRequest>()
            if (databaseService.register(body)) {
                    call.respond(HttpStatusCode.OK, "User registered successfully")
            } else {
                call.respond(HttpStatusCode.Conflict, "User Exists")
            }
        }

        post("/login") {
            val body = call.receive<LoginRequest>()
            if (databaseService.login(body.username, body.password)) {
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
            get("/tags") {
                val flattenedTags = databaseService.flattenedTags()
                call.respond(HttpStatusCode.OK, flattenedTags)
            }


            put("/tags") {
                val createTagRequest = call.receive<CreateTagRequest>()
                databaseService.createTag(createTagRequest)
                call.respond(HttpStatusCode.OK)
            }
            post("/tags/{id}") {
                val tagId = call.parameters["id"]?.toIntOrNull()
                if (tagId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid tag ID")
                    return@post
                }
                val updateTagRequest = call.receive<CreateTagRequest>()
                databaseService.updateTag(tagId, updateTagRequest)
                call.respond(HttpStatusCode.OK)
            }


            put("/tunes") {
                val createTuneRequest = call.receive<CreateTuneRequest>()
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val user = databaseService.findUserByUsername(username) ?: run {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                    return@put
                }

                databaseService.createTune(user, createTuneRequest)
                call.respond(HttpStatusCode.Created)
            }

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