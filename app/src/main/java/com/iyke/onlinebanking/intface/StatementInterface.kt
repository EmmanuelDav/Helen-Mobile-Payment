package com.iyke.onlinebanking.intface

import android.view.View
import androidx.fragment.app.Fragment
import com.iyke.onlinebanking.model.Statement

interface StatementInterface<T> {
    fun onItemClick(statement: Statement)
}