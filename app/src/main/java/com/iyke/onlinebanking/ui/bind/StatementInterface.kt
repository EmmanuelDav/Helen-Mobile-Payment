package com.iyke.onlinebanking.ui.bind

import com.iyke.onlinebanking.models.BankStatements

interface StatementInterface<T> {
    fun onItemClick(statement: BankStatements)
}