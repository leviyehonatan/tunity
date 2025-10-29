package com.leviyehonatan.tunity

import com.leviyehonatan.tunity.auth.UsersTable
import com.leviyehonatan.tunity.links.LinksTable
import com.leviyehonatan.tunity.links.TuneLinksTable
import com.leviyehonatan.tunity.links.youtube.YoutubeMetadataTable
import com.leviyehonatan.tunity.tags.TagNameTranslationsTable
import com.leviyehonatan.tunity.tags.TagsTable
import com.leviyehonatan.tunity.tags.TuneTagsTable
import com.leviyehonatan.tunity.tune.TuneNameTranslationsTable
import com.leviyehonatan.tunity.tune.TunesTable
import com.leviyehonatan.tunity.tune.UserTunesTable
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun initDb() {
    transaction {
        SchemaUtils.create(
            UsersTable,
            TagsTable,
            TunesTable,
            UserTunesTable,
            TuneNameTranslationsTable,
            TagNameTranslationsTable,
            TuneTagsTable,
            LinksTable,
            YoutubeMetadataTable,
            TuneLinksTable
        )
        addLogger(StdOutSqlLogger)
    }

}
