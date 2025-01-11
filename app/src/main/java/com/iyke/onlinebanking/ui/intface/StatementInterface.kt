package com.iyke.onlinebanking.ui.intface

import com.iyke.onlinebanking.models.Statement

interface StatementInterface<T> {
    fun onItemClick(statement: Statement)
}