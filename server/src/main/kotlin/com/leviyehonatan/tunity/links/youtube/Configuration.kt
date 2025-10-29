package com.leviyehonatan.tunity.links.youtube

import kotlinx.serialization.Serializable

@Serializable
data class YoutubeConfig(val apiKey: String, val apiUrl: String)
