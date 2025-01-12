package com.iyke.onlinebanking.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

class NetworkInformation(private val context: Context) {


    @RequiresApi(Build.VERSION_CODES.M)
    fun checkNow(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Check if a network is active and has Internet capabilities
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        if (!isConnected) {
            // Show a toast message if there's no internet connection
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
        }

        return isConnected
    }

}