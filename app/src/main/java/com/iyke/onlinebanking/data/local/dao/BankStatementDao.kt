package com.iyke.onlinebanking.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iyke.onlinebanking.data.local.entries.BankStatementsEntity

@Dao
interface BankStatementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatement(statement: BankStatementsEntity)

    @Query("SELECT * FROM db_statement WHERE id = :id")
    suspend fun getStatementsByUserId(id :Long): List<BankStatementsEntity>

}