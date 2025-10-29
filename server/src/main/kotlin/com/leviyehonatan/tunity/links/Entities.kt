package com.leviyehonatan.tunity.links

import com.leviyehonatan.tunity.links.youtube.YoutubeMetadataEntity
import com.leviyehonatan.tunity.links.youtube.YoutubeMetadataTable
import com.leviyehonatan.tunity.tune.TuneEntity
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class LinkEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LinkEntity>(LinksTable)
    val tunes by TuneEntity via TuneLinksTable
    var url by LinksTable.url
    //svar linkType by LinksTable.linkType
    val youtubeMetadata by YoutubeMetadataEntity optionalBackReferencedOn YoutubeMetadataTable.link
}
