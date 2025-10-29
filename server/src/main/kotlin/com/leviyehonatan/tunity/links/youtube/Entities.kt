package com.leviyehonatan.tunity.links.youtube

import com.leviyehonatan.tunity.links.LinkEntity
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass

class YoutubeMetadataEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, YoutubeMetadataEntity>(YoutubeMetadataTable)

    var link by LinkEntity referencedOn YoutubeMetadataTable.link
    var title by YoutubeMetadataTable.title
    var thumbnailUrl by YoutubeMetadataTable.thumbnailUrl
}
