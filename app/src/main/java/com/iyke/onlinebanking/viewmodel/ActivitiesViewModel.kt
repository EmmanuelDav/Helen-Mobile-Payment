package com.iyke.onlinebanking.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.Constants.BALANCE
import com.iyke.onlinebanking.Constants.USERS
import com.iyke.onlinebanking.model.Users

class ActivitiesViewModel(application: Application) : AndroidViewModel(application) {

    val userRef = FirebaseFirestore.getInstance().collection(USERS)


}

