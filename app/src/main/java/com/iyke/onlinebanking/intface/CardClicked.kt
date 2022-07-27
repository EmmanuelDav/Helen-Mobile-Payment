package com.iyke.onlinebanking.intface

import com.iyke.onlinebanking.model.CardDetails
import com.iyke.onlinebanking.model.Statement

interface CardClicked<T> {

    fun onCardClick(cards: CardDetails)

}