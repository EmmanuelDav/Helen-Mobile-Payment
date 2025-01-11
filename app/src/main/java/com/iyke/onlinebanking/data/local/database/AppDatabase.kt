package com.iyke.onlinebanking.data.local.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import com.iyke.onlinebanking.data.local.dao.BankStatementDao
import com.iyke.onlinebanking.data.local.dao.UsersDao
import com.iyke.onlinebanking.data.local.entries.BankStatementsEntity
import com.iyke.onlinebanking.data.local.entries.UsersEntity


@Database(entities = [UsersEntity::class, BankStatementsEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getUsersDao():UsersDao
    abstract fun getBankStatementDao():BankStatementDao

    companion object{
        const val DATABASE_NAME = "helen_db"
    }

}