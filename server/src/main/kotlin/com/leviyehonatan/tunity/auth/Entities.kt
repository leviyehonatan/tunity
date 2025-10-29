package com.leviyehonatan.tunity.auth

import com.leviyehonatan.tunity.tune.TuneEntity
import com.leviyehonatan.tunity.tune.TunesTable
import com.leviyehonatan.tunity.tune.UserTunesTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UsersTable)

    val createdTunes by TuneEntity referrersOn TunesTable.createdBy
    var tunes by TuneEntity via UserTunesTable
    var username by UsersTable.username
    var hashedPassword by UsersTable.hashedPassword
}
