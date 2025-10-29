package com.leviyehonatan.tunity.tags

import com.leviyehonatan.tunity.CreateTagRequest
import com.leviyehonatan.tunity.Translation
import com.leviyehonatan.tunity.TuneTag
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun createTag(createTagRequest: CreateTagRequest): TagEntity =
    transaction {
        val newTag = TagEntity.new {

        }

        createTagRequest.nameTranslations.forEach {
            TagNameTranslationEntity.new {
                language = it.language
                translation = it.translation
                tag = newTag
            }
        }
        newTag
    }

fun updateTag(tagId: Int, updateTagRequest: CreateTagRequest): TagEntity =
    transaction {
        val tag = TagEntity[tagId]
        // Handle parent update
        if (updateTagRequest.parentId != null) {
            tag.parent = TagEntity[updateTagRequest.parentId!!]
        } else if (tag.parent != null) {
            // If parentId is null in request but tag has a parent, remove the parent
            tag.parent = null
        }

        val existingTranslations = tag.tagNameTranslations.associateBy { it.language }
        val updatedTranslations = updateTagRequest.nameTranslations.associateBy { it.language }

        // Handle deletions
        existingTranslations.forEach { (language, entity) ->
            if (language !in updatedTranslations) {
                entity.delete()
            }
        }

        // Handle additions and updates
        updatedTranslations.forEach { (language, translationRequest) ->
            val existingTranslation = existingTranslations[language]
            if (existingTranslation != null) {
                // Update existing translation if different
                if (existingTranslation.translation != translationRequest.translation) {
                    existingTranslation.translation = translationRequest.translation
                }
            } else {
                // Add new translation
                TagNameTranslationEntity.new {
                    this.language = language
                    this.translation = translationRequest.translation
                    this.tag = tag
                }
            }
        }
        tag
    }

fun flattenedTags() =
    transaction {
        TagEntity.all().map {
            TuneTag(
                it.id.value,
                it.tagNameTranslations.map { tagTranslation ->
                    Translation(
                        tagTranslation.language,
                        tagTranslation.translation
                    )
                },
                listOf()
            )
        }
    }
