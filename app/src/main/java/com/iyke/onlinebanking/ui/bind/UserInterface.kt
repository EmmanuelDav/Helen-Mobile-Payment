package com.iyke.onlinebanking.ui.bind

import com.iyke.onlinebanking.models.Users

interface UserInterface<T> {
    fun onItemClick(user : Users)
}