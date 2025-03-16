package com.iyke.onlinebanking.data.local.entries

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "db_users",
        indices = [Index(value = ["userId"], unique = true)] // Add a unique index on userId
)
data class UsersEntity(

    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Optional primary key

    var userId:Long? = 0,
    var name:String?="",

    var email:String?="",

    var phoneNumber:String?="",

    var profileUrl:String?="",

    var balance:String?=""
)

