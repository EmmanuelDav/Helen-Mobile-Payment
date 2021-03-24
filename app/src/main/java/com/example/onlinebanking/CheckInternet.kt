package com.example.onlinebanking

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService

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