package com.leviyehonatan.tunity.tune

import com.leviyehonatan.tunity.shared.CreateTuneRequest
import com.leviyehonatan.tunity.auth.getUser
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tuneRoutes() {
    authenticate("auth-jwt") {
        put("/tunes") {
            val createTuneRequest = call.receive<CreateTuneRequest>()

            val user = call.getUser()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@put
            }

            val tune = createTune(user, createTuneRequest, call.application)
            call.respond(HttpStatusCode.Created, tune.toTune())
        }
    }
}

