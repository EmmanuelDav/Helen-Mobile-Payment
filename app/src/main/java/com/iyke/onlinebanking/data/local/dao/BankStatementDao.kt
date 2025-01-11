package com.iyke.onlinebanking.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iyke.onlinebanking.data.local.entries.BankStatementsEntity


interface BankStatementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatement(statement: BankStatementsEntity)

    @Query("SELECT * FROM db_statement WHERE userId = :id")
    suspend fun getStatementsByUserId(id :Long): List<BankStatementsEntity>

    @Delete
    suspend fun deleteStatement(id:Long)

}