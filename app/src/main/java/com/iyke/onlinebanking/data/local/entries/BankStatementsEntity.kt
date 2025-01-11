package com.iyke.onlinebanking.data.local.entries

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.common.collect.Multimaps.index
import com.google.firebase.Timestamp


@Entity(
    tableName = "db_statement", foreignKeys = [ForeignKey(
        entity = UsersEntity::class,
        parentColumns = ["userId"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE
    )], indices = [index("userId")]
)

data class BankStatementsEntity(
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0,

    var amount: String? = "",

    var type: String? = "",

    var sender: String? = "",
    var timestamp: Timestamp,
    var message: String? = ""
)