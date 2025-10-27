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

            val nameTranslations = SizedCollection(createTuneRequest.tune.nameTranslations.map {
                TuneNameTranslationEntity.new {
                    language = it.language
                    tune = tuneEntity
                    translation = it.translation
                }
            })

            tuneEntity
        }


    fun flattenedTags() =
        transaction {
            TagEntity.all().map {
                TuneTag(
                    it.id.value,
                    it.tagNameTranslations.map { Translation(it.language, it.translation) },
                    listOf()
                )
            }
        }

    fun tags() =
        transaction { TagEntity.all().toList() }

    fun createTag(createTagRequest: CreateTagRequest): TagEntity =
        transaction {
            val newTag = TagEntity.new {

            }

            val translations = createTagRequest.nameTranslations.map {
                TagNameTranslationEntity.new {
                    language = it.language
                    translation = it.translation
                    tag = newTag
                }
            }
            newTag
        }

    fun findUserByUsername(username: String): UserEntity? =
        transaction {
            UserEntity.find { UsersTable.username eq username }.singleOrNull()
        }

}