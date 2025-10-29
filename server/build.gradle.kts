import java.util.Properties

// 1. Define the path and load properties lazily
//    "lazy" means this code only runs once, the first time it's needed
//    (either by "run" or "test").
val localProperties by lazy {
    val localPropsFile = rootProject.file("local.properties")
    if (localPropsFile.exists()) {
        Properties().apply {
            localPropsFile.inputStream().use { load(it) }
        }.entries.associate {
            it.key.toString() to it.value.toString()
        }
    } else {
        emptyMap()
    }
}

// 2. Apply properties to the "run" task (of type JavaExec)
tasks.named("run", JavaExec::class) {
    systemProperties.putAll(localProperties)
}

// 3. Apply properties to ALL tasks of type "Test"
tasks.withType<Test> {
    systemProperties.putAll(localProperties)
}

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

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)

    implementation(libs.exposed.migration.core)
    implementation(libs.exposed.migration.jdbc)

//    implementation(libs.google.api.client)
//    implementation(libs.google.oauth.client)
//    implementation(libs.google.api.services.youtube)

    implementation(libs.bcrypt)

    implementation(libs.jdbc.postgres)

    testImplementation(libs.ktor.client.contentNegotiation)
    testImplementation(libs.h2)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    implementation(libs.slf4j.simple)
}
