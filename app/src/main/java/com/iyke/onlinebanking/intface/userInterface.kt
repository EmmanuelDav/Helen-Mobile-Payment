package com.iyke.onlinebanking.intface

import com.iyke.onlinebanking.model.Users

interface UserInterface<T> {
    fun onItemClick(user : Users)
}