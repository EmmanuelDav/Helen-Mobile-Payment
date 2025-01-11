package com.iyke.onlinebanking.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

class CheckInternet(private val context: Context) {


    fun checkNow():Boolean
    {
        val cm  = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo

        if(networkInfo != null && networkInfo.isConnected)
        {
            return true
        }
        else
            Toast.makeText(context,"No internet Connection", Toast.LENGTH_SHORT).show()
            return false
    }

}