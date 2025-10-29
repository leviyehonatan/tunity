package com.leviyehonatan.tunity.links.youtube

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass

class YoutubeLinkEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, YoutubeLinkEntity>(YoutubeLinksTable)

    var title by YoutubeLinksTable.title
    var thumbnailUrl by YoutubeLinksTable.thumbnailUrl
}
