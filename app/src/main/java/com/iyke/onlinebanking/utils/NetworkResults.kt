package com.iyke.onlinebanking.utils



sealed class NetworkResults<out T> {
    object Idle : NetworkResults<Nothing>()
    object Loading : NetworkResults<Nothing>()
    data class Success<T>(val data: T) : NetworkResults<T>()

    data class Error(val message: String) : NetworkResults<Nothing>()
}