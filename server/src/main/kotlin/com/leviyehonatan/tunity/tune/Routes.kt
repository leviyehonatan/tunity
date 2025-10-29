package com.leviyehonatan.tunity.tune

import com.leviyehonatan.tunity.shared.CreateTuneRequest
import com.leviyehonatan.tunity.auth.getUser
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.SizedCollection
import org.jetbrains.exposed.v1.jdbc.SizedIterable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Route.tuneRoutes() {
    authenticate("auth-jwt") {
        put("/tunes") {
            val createTuneRequest = call.receive<CreateTuneRequest>()

            val user = call.getUser()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@put
            }

            val tuneEntity = createTune(user, createTuneRequest, call.application)
            val tune = tuneEntity.toTune()
            call.respond(HttpStatusCode.Created, tune)
        }

        get("/tunes/my") {
            val user = call.getUser()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }

            val tunes = transaction { user.tunes.map { it.toTune() } }
            call.respond(tunes)
        }

        get("/tunes/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val user = call.getUser()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }
            val tune = user.tunes.find { it.id.value == id }
            if (tune == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(tune.toTune())
        }

        put("/tunes/{id}/add-to-my-tunes") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val user = call.getUser()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@put
            }

            val tune = TuneEntity.findById(id)
            if (tune == null) {
                call.respond(HttpStatusCode.NotFound)
                return@put
            }

            user.tunes = SizedCollection(user.tunes + tune)
            call.respond(HttpStatusCode.OK)
        }

        put("/tunes/{id}/remove-from-my-tunes") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val user = call.getUser()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@put
            }

            val tune = TuneEntity.findById(id)
            if (tune == null) {
                call.respond(HttpStatusCode.NotFound)
                return@put
            }

            user.tunes = SizedCollection(user.tunes - tune)
            call.respond(HttpStatusCode.OK)
        }
    }
}

