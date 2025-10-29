package com.leviyehonatan.tunity.links

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class LinkEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LinkEntity>(LinksTable)

    var url by LinksTable.url
}
