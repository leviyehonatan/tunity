package com.leviyehonatan.tunity.links

import com.leviyehonatan.tunity.tune.TunesTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable


enum class LinkType {
    NONE,
    YOUTUBE,
}

object LinksTable : IntIdTable() {
    val url = varchar("url", 1024)
    //val linkType = enumeration("link_type", LinkType::class)
}

object TuneLinksTable : Table() {
    val tune = reference("tune", TunesTable)
    val link = reference("link", LinksTable)
    override val primaryKey = PrimaryKey(tune, link)
}

