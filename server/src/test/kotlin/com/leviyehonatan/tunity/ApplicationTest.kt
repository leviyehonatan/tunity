package com.leviyehonatan.tunity

import com.leviyehonatan.tunity.data.ExposedUser
import com.leviyehonatan.tunity.plugins.configureDatabases
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.*
import org.jetbrains.exposed.v1.jdbc.Database
import kotlin.test.*


fun createMemoryDb() = Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
            configureDatabases(createMemoryDb())
        }

        client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }


        val response1 = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(NewUserRegistration(username = "test123", password = "test123"))

        }

        val response2 = client.post
    }
}