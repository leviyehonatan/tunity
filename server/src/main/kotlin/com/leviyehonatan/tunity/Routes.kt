package com.leviyehonatan.tunity
import com.leviyehonatan.tunity.auth.authRoutes
import com.leviyehonatan.tunity.plugins.authentication
import com.leviyehonatan.tunity.plugins.generalRoutes
import com.leviyehonatan.tunity.tune.tuneRoutes
import com.leviyehonatan.tunity.links.youtube.youtubeRoutes
import com.leviyehonatan.tunity.tags.tagRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.routing

fun Application.routes() {
    routing {
        youtubeRoutes()
        authRoutes()
        tuneRoutes()
        generalRoutes()
        authRoutes()
        tagRoutes()
    }

}