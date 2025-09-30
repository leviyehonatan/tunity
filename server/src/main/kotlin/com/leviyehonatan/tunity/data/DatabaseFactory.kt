package com.leviyehonatan.tunity.data

import org.jetbrains.exposed.v1.jdbc.Database

fun createDatabase() = Database.connect(
    "jdbc:postgresql://localhost:5432/test123",
    driver = "org.postgresql.Driver",
    user = "nickst",
    password = ""
)
