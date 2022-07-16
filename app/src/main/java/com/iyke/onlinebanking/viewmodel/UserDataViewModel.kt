package com.iyke.onlinebanking.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.Constants.USERS
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.model.Users


class UserDataViewModel(application: Application) : AndroidViewModel(application) {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

     var userData = MutableLiveData<Users>()

    fun fetchUserDetails() {
        var user:Users?= null
        db.collection(USERS).document(FirebaseAuth.getInstance().currentUser?.email.toString())
            .get().addOnSuccessListener { doc ->
                user = doc.toObject(Users::class.java)
                userData.value = user

        }.addOnFailureListener { Log.d("VerifyActivity", "Log in failed because ${it.message}")

        }
    }

    fun navigateView(view: View){
        when(view.id){
            R.id.addFunds -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addMoney)
            R.id.sendMoney -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_sentFragment)
            R.id.see -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_historyFragment)
        }
    }

}