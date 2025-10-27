package com.leviyehonatan.tunity.data

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass


object UsersTable : IntIdTable() {
    val username = varchar("username", length = 50)
    val hashedPassword = varchar("hashedPassword", 256)
}
object TuneTagsTable : Table() {
    val tune = reference("tune", TunesTable)
    val tag = reference("tag", TagsTable)
    override val primaryKey = PrimaryKey(tune, tag)
}

object TunesTable : IntIdTable() {
    val createdBy = reference("createdBy", UsersTable)
}

object TuneNameTranslationsTable : IntIdTable()  {
    val tune = reference("tune", TunesTable)
    val language = varchar("language", 10)
    val translation = varchar("translation", 256)
}

object TagNameTranslationsTable : IntIdTable()  {
    val tag = reference("tag", TagsTable)
    val language = varchar("language", 10)
    val translation = varchar("translation", 256)
}

object TagsTable : IntIdTable() {
    val parent = optReference("parent", TagsTable)
}


