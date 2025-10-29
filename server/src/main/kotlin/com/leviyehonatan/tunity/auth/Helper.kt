package com.leviyehonatan.tunity.auth

import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.RoutingCall

fun RoutingCall.getUsername(): String? {
    val principal = principal<JWTPrincipal>()
    return principal!!.payload.getClaim("username").asString()
}

fun RoutingCall.getUser(): UserEntity? {
    return findUserByUsername(getUsername()!!)

}