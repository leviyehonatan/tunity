package com.leviyehonatan.tunity.plugins

// plugins/Security.kt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.property
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

@Serializable
data class JwtConfig(val realm: String, val secret: String, val issuer: String, val audience: String)

fun Application.authentication() {

    val jwtConfig: JwtConfig = property("jwt")

    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtConfig.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtConfig.secret))
//                        .withAudience(jwtConfig.audience)
//                        .withIssuer(jwtConfig.issuer)
                        .build()
            )

            validate {
                if (it.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(it.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}