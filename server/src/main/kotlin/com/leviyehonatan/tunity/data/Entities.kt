package com.leviyehonatan.tunity.data

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    var username by UsersTable.username
    var hashedPassword by UsersTable.hashedPassword
}


class TuneEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TuneEntity>(TunesTable)

    val tuneNameTranslations by TuneNameTranslationEntity referrersOn TuneNameTranslationsTable.tune
    var tags by TagEntity via TuneTagsTable
    var createdBy by UserEntity referencedOn TunesTable.createdBy
}


class TuneNameTranslationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TuneNameTranslationEntity>(TuneNameTranslationsTable)

    var tune by TuneEntity referencedOn TuneNameTranslationsTable.tune
    var language by TuneNameTranslationsTable.language
    var translation by TuneNameTranslationsTable.translation
}

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
