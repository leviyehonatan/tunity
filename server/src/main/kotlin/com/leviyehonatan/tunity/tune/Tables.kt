package com.leviyehonatan.tunity.tune

import com.leviyehonatan.tunity.auth.UsersTable
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable


object TunesTable : IntIdTable() {
    val createdBy = reference("createdBy", UsersTable)
}

object TuneNameTranslationsTable : IntIdTable() {
    val tune = reference("tune", TunesTable)
    val language = varchar("language", 10)
    val translation = varchar("translation", 256)
}


