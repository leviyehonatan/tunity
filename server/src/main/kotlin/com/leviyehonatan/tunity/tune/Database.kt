package com.leviyehonatan.tunity.tune

import com.leviyehonatan.tunity.shared.CreateTuneRequest
import com.leviyehonatan.tunity.shared.Translation
import com.leviyehonatan.tunity.shared.Tune
import com.leviyehonatan.tunity.auth.UserEntity
import com.leviyehonatan.tunity.links.LinkEntity
import com.leviyehonatan.tunity.links.LinksTable
import com.leviyehonatan.tunity.tags.TagEntity
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.SizedCollection
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun createTune(user: UserEntity, createTuneRequest: CreateTuneRequest): TuneEntity =
    transaction {
        val tuneEntity = TuneEntity.new {
            createdBy = user
            tags = SizedCollection(createTuneRequest.tune.tagIds.map { TagEntity[it] })
        }

        createTuneRequest.tune.nameTranslations.forEach {
            TuneNameTranslationEntity.new {
                language = it.language
                tune = tuneEntity
                translation = it.translation
            }
        }

        val linkEntities = createTuneRequest.tune.links.map { url ->
            LinkEntity.find { LinksTable.url eq url }.singleOrNull() ?: LinkEntity.new {
                this.url = url
            }
        }
        tuneEntity.links = SizedCollection(linkEntities)

        tuneEntity
    }


fun TuneEntity.toTune() = transaction {
    Tune(
        id = this@toTune.id.value,
        nameTranslations = tuneNameTranslations.map {
            Translation(
                language = it.language,
                translation = it.translation
            )
        },
        links = links.map {
            it.url

        },
        tagIds = tags.map { it.id.value  }
    )
}