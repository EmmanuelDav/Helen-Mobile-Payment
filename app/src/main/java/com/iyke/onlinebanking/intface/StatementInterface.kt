package com.iyke.onlinebanking.intface

import com.iyke.onlinebanking.model.Statement

interface StatementInterface<T> {
    fun onItemClick(statement: Statement )
}