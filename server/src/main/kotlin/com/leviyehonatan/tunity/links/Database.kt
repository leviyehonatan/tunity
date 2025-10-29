package com.leviyehonatan.tunity.links

import com.leviyehonatan.tunity.links.youtube.YoutubeMetadataEntity
import com.leviyehonatan.tunity.links.youtube.YoutubeMetadataTable
import com.leviyehonatan.tunity.links.youtube.YoutubeService
import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

suspend fun getOrCreateLink(url: String, application: Application): LinkEntity {
    val youtubeId = extractYoutubeId(url)
    return if (youtubeId != null) {
        val youtubeMetadata = transaction {  YoutubeMetadataEntity.findById(youtubeId) }
        if (youtubeMetadata != null) {
            youtubeMetadata.link
        } else {
            // If YouTube metadata doesn't exist, create a new link and then the metadata
            return YoutubeService(application).getYoutubeLink(youtubeId)
        }

    } else {
        // Not a YouTube link, check if the URL already exists in the LinkTable
        return transaction {
            LinkEntity.find { LinksTable.url eq url }.firstOrNull() ?: LinkEntity.new {
                this.url = url
            }
        }
    }
}

private fun extractYoutubeId(url: String): String? {
    val youtubeRegex = Regex("^(https?://)?(www\\.)?(youtube\\.com|youtu\\.be)/watch\\?v=([a-zA-Z0-9_-]{11}).*$")
    val matchResult = youtubeRegex.find(url)
    return matchResult?.groups?.get(4)?.value
}
