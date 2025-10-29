package com.leviyehonatan.tunity.links.youtube

import com.leviyehonatan.tunity.links.LinksTable
import com.leviyehonatan.tunity.tune.TunesTable
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable

object YoutubeMetadataTable : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("id", 11).entityId()
    val link = reference("link_id", LinksTable, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val title = varchar("title", 256)
    val thumbnailUrl = varchar("thumbnailUrl", 1024)
    override val primaryKey = PrimaryKey(id)
}