package com.iyke.onlinebanking.data.local.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iyke.onlinebanking.data.local.dao.BankStatementDao
import com.iyke.onlinebanking.data.local.dao.UsersDao
import com.iyke.onlinebanking.data.local.entries.BankStatementsEntity
import com.iyke.onlinebanking.data.local.entries.UsersEntity


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // migration for adding a new column
        database.execSQL("ALTER TABLE articles ADD COLUMN new_column_name TEXT DEFAULT ''")
    }
}

@Database(entities = [UsersEntity::class, BankStatementsEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getUsersDao():UsersDao
    abstract fun getBankStatementDao():BankStatementDao

    companion object{
        const val DATABASE_NAME = "helen_db"
    }

}