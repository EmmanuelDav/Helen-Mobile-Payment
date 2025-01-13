package com.iyke.onlinebanking.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iyke.onlinebanking.data.local.entries.UsersEntity
import com.iyke.onlinebanking.models.Users

interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: UsersEntity):Long

    @Query("SELECT * FROM db_users WHERE userId = :id")
    suspend fun getUserByID(id: String):UsersEntity

    @Delete
    suspend fun deleteUser(users: UsersEntity)

    @Query("SELECT * FROM db_users LIMIT 1")
    suspend fun getExistingUser(): UsersEntity?

}