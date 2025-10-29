package com.leviyehonatan.tunity.links.youtube

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.youtubeRoutes() {
    val youtubeService = YoutubeService(application)
    post("/youtube") {
        val body = call.receive<YoutubeRequest>()
        val videoId = extractVideoIdFromUrl(body.url)
        if (videoId == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid YouTube URL")
            return@post
        }
        val youtubeLink = youtubeService.getYoutubeLink(videoId)
        call.respond(HttpStatusCode.OK, youtubeLink)
    }
}


fun extractVideoIdFromUrl(url: String): String? {
    val regex =
        "(?:https://?://)?(?:www\\.)?(?:m\\.)?(?:youtube\\.com|youtu\\.be)/(?:watch\\?v=|embed/|v/|)([\\w-]+)(?:\\S+)?".toRegex()
    return regex.find(url)?.groupValues?.get(1)
}
