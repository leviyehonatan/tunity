package com.leviyehonatan.tunity

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(val username: String, val password: String)

@Serializable
data class NewUserRegistration(val username: String, val password: String)