package com.leviyehonatan.tunity.tune

import com.leviyehonatan.tunity.auth.UsersTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable


object TunesTable : IntIdTable() {
    val createdBy = reference("createdBy", UsersTable)
}

object TuneNameTranslationsTable : IntIdTable() {
    val tune = reference("tune", TunesTable)
    val language = varchar("language", 10)
    val translation = varchar("translation", 256)
}

object UserTunesTable : Table() {
    val user = reference("user", UsersTable)
    val tune = reference("tune", TunesTable)
    override val primaryKey = PrimaryKey(user, tune, name = "PK_UserTunes")

}

