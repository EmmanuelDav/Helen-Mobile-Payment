package com.iyke.onlinebanking.ui.intface

import com.iyke.onlinebanking.models.CardDetails

interface CardClicked<T> {
    fun onCardClick(cards: CardDetails)
}