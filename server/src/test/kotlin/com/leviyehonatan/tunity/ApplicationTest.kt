package com.leviyehonatan.tunity

import com.leviyehonatan.tunity.data.ExposedUser
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

        client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }


        val response2 = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(NewUserRegistration(username = "test123", password = "test123"))
        }
    }
}