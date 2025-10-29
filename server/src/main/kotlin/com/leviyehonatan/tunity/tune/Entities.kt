package com.leviyehonatan.tunity.tune

import com.leviyehonatan.tunity.auth.UserEntity
import com.leviyehonatan.tunity.links.LinkEntity
import com.leviyehonatan.tunity.links.TuneLinksTable
import com.leviyehonatan.tunity.links.youtube.TuneYoutubeLinksTable
import com.leviyehonatan.tunity.links.youtube.YoutubeLinkEntity
import com.leviyehonatan.tunity.tags.TagEntity
import com.leviyehonatan.tunity.tags.TuneTagsTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class TuneEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TuneEntity>(TunesTable)

    val tuneNameTranslations by TuneNameTranslationEntity referrersOn TuneNameTranslationsTable.tune
    var tags by TagEntity via TuneTagsTable
    var links by LinkEntity via TuneLinksTable
    var youtubeLinks by YoutubeLinkEntity via TuneYoutubeLinksTable
    var createdBy by UserEntity referencedOn TunesTable.createdBy
}


class TuneNameTranslationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TuneNameTranslationEntity>(TuneNameTranslationsTable)

    var tune by TuneEntity referencedOn TuneNameTranslationsTable.tune
    var language by TuneNameTranslationsTable.language
    var translation by TuneNameTranslationsTable.translation
}

