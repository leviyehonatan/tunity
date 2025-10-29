package com.leviyehonatan.tunity.shared

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class RegisterRequest(val username: String, val password: String)

@Serializable
data class Translation(val language: String, val translation: String)

@Serializable
data class TuneTag(val id: Int, val translations: List<Translation>, val path: List<String>)

@Serializable
data class Tune(
    val id: Int?,
    val nameTranslations: List<Translation>,
    val tagIds: List<Int>,
    val links: List<String> = emptyList()
)

@Serializable
data class CreateTuneRequest(val tune: Tune)

@Serializable
data class CreateTagRequest(val nameTranslations: List<Translation>, val parentId: Int? = null)
