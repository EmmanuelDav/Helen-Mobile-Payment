package com.iyke.onlinebanking.ui.bind

import com.iyke.onlinebanking.models.CardInfo
import com.iyke.onlinebanking.ui.card.CardDetails

interface CardClicked<T> {
    fun onCardClick(cards: CardInfo)
}