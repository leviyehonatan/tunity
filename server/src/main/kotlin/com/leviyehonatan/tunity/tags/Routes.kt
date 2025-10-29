package com.leviyehonatan.tunity.tags

import com.leviyehonatan.tunity.shared.CreateTagRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.*

fun Route.tagRoutes() {
    get("/tags") {
        call.respond(HttpStatusCode.OK, flattenedTags())
    }


    authenticate("auth-jwt") {
        put("/tags") {
            val createTagRequest = call.receive<CreateTagRequest>()
            createTag(createTagRequest)
            call.respond(HttpStatusCode.OK)
        }
        post("/tags/{id}") {
            val tagId = call.parameters["id"]?.toIntOrNull()
            if (tagId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid tag ID")
                return@post
            }
            val updateTagRequest = call.receive<CreateTagRequest>()
            updateTag(tagId, updateTagRequest)
            call.respond(HttpStatusCode.OK)
        }
    }
}