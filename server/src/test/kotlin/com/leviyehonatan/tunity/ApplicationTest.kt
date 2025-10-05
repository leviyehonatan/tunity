package com.leviyehonatan.tunity

import com.leviyehonatan.tunity.plugins.authRoutes
import com.leviyehonatan.tunity.plugins.authentication
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.*
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.test.*


class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        environment {
            config = ApplicationConfig("application.conf")
        }
        application {
            module()
        }

        client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response1 = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(username = "test123", password = "test123"))
        }

        assertEquals(HttpStatusCode.OK, response1.status)

        val response2 = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username = "test123", password = "test123"))
        }

        assertEquals(HttpStatusCode.OK, response2.status)

        val token = response2.body<HashMap<String, String>>()
        assertNotNull(token["token"])

        val response3 = client.get("/hello") {
            header(HttpHeaders.Authorization, "Bearer ${token["token"]}")
        }
        assertEquals(HttpStatusCode.OK, response3.status)

    }
}