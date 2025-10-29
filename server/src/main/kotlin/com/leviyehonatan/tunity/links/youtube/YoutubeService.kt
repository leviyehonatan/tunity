package com.leviyehonatan.tunity.links.youtube

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class YoutubeService(private val application: Application) {
    suspend fun getYoutubeLink(videoId: String): YoutubeLinkEntity {
        val existingLink = transaction {
            YoutubeLinkEntity.findById(videoId)
        }

        if (existingLink != null) {
            return existingLink
        }

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
        val youtubeConfig: YoutubeConfig = application.property("youtube")
        val response: YoutubeVideoResponse = client.get(youtubeConfig.apiUrl) {
            url {
                parameters.append("id", videoId)
                parameters.append("key", youtubeConfig.apiKey)
                parameters.append("part", "snippet")
            }
        }.body()

        val video = response.items.first()

        return transaction {
            YoutubeLinkEntity.new(videoId) {
                this.title = video.snippet.title
                this.thumbnailUrl = video.snippet.thumbnails.high.url
            }
        }
    }
}
