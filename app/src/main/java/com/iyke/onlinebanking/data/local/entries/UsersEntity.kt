package com.iyke.onlinebanking.data.local.entries

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "db_users")
data class UsersEntity(

    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Optional primary key

    var userId:String? = "",
    var name:String?="",

    var email:String?="",

    var phoneNumber:String?="",

    var profileUrl:String?="",

    var balance:String?=""
)

