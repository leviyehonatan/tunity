package com.leviyehonatan.tunity.links.youtube

import com.leviyehonatan.tunity.links.LinkEntity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class YoutubeService(private val application: Application) {
    suspend fun getYoutubeLink(videoId: String): LinkEntity {
        val existingLink = transaction {
            YoutubeMetadataEntity.findById(videoId)
        }

        if (existingLink != null) {
            return existingLink.link
        }

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })

            }
        }
        val youtubeConfig: YoutubeConfig = application.property("youtube")
        val response: YoutubeVideoResponse = client.get(youtubeConfig.apiUrl) {
            url {
                appendPathSegments("videos")
                parameters.append("id", videoId)
                parameters.append("key", youtubeConfig.apiKey)
                parameters.append("part", "snippet")
            }
        }.body()

        val video = response.items.first()

        return transaction {
            val newLink = LinkEntity.new {
                url = "https://www.youtube.com/watch?v=${videoId}"
            }
            YoutubeMetadataEntity.new(videoId) {
                title = video.snippet.title
                thumbnailUrl = video.snippet.thumbnails.high.url
                link = newLink
            }
            newLink
        }
    }
}
