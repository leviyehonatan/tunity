plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    alias(libs.plugins.kotlinPluginSerialization)
}

group = "com.leviyehonatan.tunity"
version = "1.0.0"
application {
    mainClass.set("com.leviyehonatan.tunity.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverCors)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.serverAuth)
    implementation(libs.ktor.serverAuthJwt)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)

    implementation(libs.exposed.migration.core)
    implementation(libs.exposed.migration.jdbc)

    implementation(libs.jdbc.postgres)

    testImplementation(libs.ktor.client.contentNegotiation)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}