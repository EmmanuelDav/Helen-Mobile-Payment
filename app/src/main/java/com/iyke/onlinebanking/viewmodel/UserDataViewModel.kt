package com.iyke.onlinebanking.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.Constants.USERS
import com.iyke.onlinebanking.model.Users


class UserDataViewModel(application: Application) : AndroidViewModel(application) {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    var user:Users?= null

    fun fetchUserDetails() :Users{
        db.collection(USERS).get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    user = doc.toObject(Users::class.java)
                }
            }
            .addOnFailureListener { }
        return user!!
    }

}