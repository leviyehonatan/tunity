package com.leviyehonatan.tunity.links.youtube

import kotlinx.serialization.Serializable

@Serializable
data class YoutubeVideoResponse(
    val items: List<YoutubeVideoItem>
)

@Serializable
data class YoutubeVideoItem(
    val id: String,
    val snippet: YoutubeVideoSnippet
)

@Serializable
data class YoutubeVideoSnippet(
    val title: String,
    val thumbnails: YoutubeVideoThumbnails
)

@Serializable
data class YoutubeVideoThumbnails(
    val default: YoutubeVideoThumbnail,
    val medium: YoutubeVideoThumbnail,
    val high: YoutubeVideoThumbnail
)

@Serializable
data class YoutubeVideoThumbnail(
    val url: String,
    val width: Int,
    val height: Int
)

@Serializable
data class YoutubeRequest(
    val url: String
)
