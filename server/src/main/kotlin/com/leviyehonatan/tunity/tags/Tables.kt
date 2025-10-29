package com.leviyehonatan.tunity.tags

import com.leviyehonatan.tunity.tune.TunesTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object TuneTagsTable : Table() {
    val tune = reference("tune", TunesTable)
    val tag = reference("tag", TagsTable)
    override val primaryKey = PrimaryKey(tune, tag)
}

object TagsTable : IntIdTable() {
    val parent = optReference("parent", TagsTable)
}

object TagNameTranslationsTable : IntIdTable() {
    val tag = reference("tag", TagsTable)
    val language = varchar("language", 10)
    val translation = varchar("translation", 256)
}
