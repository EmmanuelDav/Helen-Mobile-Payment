package com.iyke.onlinebanking.data.local.entries

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.common.collect.Multimaps.index
import com.google.firebase.Timestamp


@Entity(
    tableName = "db_statement",
    foreignKeys = [
        ForeignKey(
            entity = UsersEntity::class,
            parentColumns = ["userId"], // References userId in UsersEntity
            childColumns = ["userId"], // userId in BankStatementsEntity
            onDelete = ForeignKey.CASCADE // Optional: Define onDelete behavior
        )
    ],
    indices = [Index(value = ["userId"])] // Add an index on userId
)
data class BankStatementsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long, // Primary key is a different column
    val userId: Long, // Foreign key to UsersEntity
    val amount: String? = "",
    val type: String? = "",
    val sender: String? = "",
    val timestamp: Long,
    val message: String? = ""
)