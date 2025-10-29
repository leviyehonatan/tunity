package com.leviyehonatan.tunity.links.youtube

import com.leviyehonatan.tunity.tune.TunesTable
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable

object YoutubeLinksTable : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("id", 11).entityId()
    val title = varchar("title", 256)
    val thumbnailUrl = varchar("thumbnailUrl", 1024)
    override val primaryKey = PrimaryKey(id)
}

object TuneYoutubeLinksTable : Table() {
    val tune = reference("tune", TunesTable)
    val youtubeLink = reference("youtubeLink", YoutubeLinksTable)
    override val primaryKey = PrimaryKey(tune, youtubeLink)
}
