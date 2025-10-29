package com.leviyehonatan.tunity.tags

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class TagNameTranslationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TagNameTranslationEntity>(TagNameTranslationsTable)

    var tag by TagEntity referencedOn TagNameTranslationsTable.tag
    var language by TagNameTranslationsTable.language
    var translation by TagNameTranslationsTable.translation
}

class TagEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TagEntity>(TagsTable)

    val tagNameTranslations by TagNameTranslationEntity referrersOn TagNameTranslationsTable.tag

    var parent by TagEntity optionalReferencedOn TagsTable.parent

}
