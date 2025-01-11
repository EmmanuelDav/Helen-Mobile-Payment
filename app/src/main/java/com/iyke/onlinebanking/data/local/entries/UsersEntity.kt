package com.iyke.onlinebanking.data.local.entries

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "db_users")
data class UsersEntity(

    @PrimaryKey(autoGenerate = true)

    var userId:Long = 0,
    var name:String?="",

    var email:String?="",

    var phoneNumber:String?="",

    var profileUrl:String?="",

    var balance:String?=""
)

