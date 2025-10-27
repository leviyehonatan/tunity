package com.leviyehonatan.tunity.data

import com.leviyehonatan.tunity.CreateTagRequest
import com.leviyehonatan.tunity.CreateTuneRequest
import com.leviyehonatan.tunity.RegisterRequest
import com.leviyehonatan.tunity.Translation
import com.leviyehonatan.tunity.TuneTag
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.mindrot.jbcrypt.BCrypt


class DatabaseService() {

    init {
        transaction {
            SchemaUtils.create(
                UsersTable,
                TagsTable,
                TunesTable,
                TuneNameTranslationsTable,
                TagNameTranslationsTable,
                TuneTagsTable
            )
            addLogger(StdOutSqlLogger)
        }
    }

    fun register(registerRequest: RegisterRequest): Boolean =
        transaction {
            if (UserEntity.find { UsersTable.username eq registerRequest.username }.count() > 0) {
                false
            } else {
                val hashed = BCrypt.hashpw(registerRequest.password, BCrypt.gensalt())
                UserEntity.new {
                    username = registerRequest.username
                    hashedPassword = hashed
                }
                true
            }
        }

    fun login(username: String, password: String) =
        transaction {
            UserEntity.find { UsersTable.username eq username }
                .map { BCrypt.checkpw(password, it.hashedPassword) }
                .singleOrNull() ?: false
        }

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

            tuneEntity
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

    fun updateTag(tagId: Int, updateTagRequest: CreateTagRequest) {
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
        }
    }


    fun findUserByUsername(username: String): UserEntity? =
        transaction {
            UserEntity.find { UsersTable.username eq username }.singleOrNull()
        }

}