package com.iyke.onlinebanking.viewmodel

import android.R.attr.name
import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.iyke.onlinebanking.Constants.PREFERENCE


class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private var authStateListener: AuthStateListener? = null

    private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginWithEmailAndPassword(email: String?, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email!!, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser)

                } else {
                    Toast.makeText(
                        context.applicationContext,
                        "Login Failure: " + task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun registerWithEmailAndPassword(email: String?, password: String?,name: String) {
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    saveUserDataWithSharedPreference(email, name, "null")
                    userLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    Toast.makeText(
                        context.applicationContext,
                        "Registration Failure: " + task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun saveUserDataWithSharedPreference(email: String, name:String, profilePic:String){
        context.getSharedPreferences(PREFERENCE, MODE_PRIVATE).let {
            val myEdit = it.edit()
            myEdit.putString("email", email)
            myEdit.putString("full_Name", name)
            myEdit.putString("picture", profilePic)
            myEdit.apply()
        }
    }

    private fun firebaseLogin(idToken: String) {
        GoogleAuthProvider.getCredential(idToken, null).let { it ->
            firebaseAuth.signInWithCredential(it)
                .addOnCompleteListener(OnCompleteListener {
                    if (it.isSuccessful){
                        saveUserDataWithSharedPreference(firebaseAuth.currentUser!!.email.toString(),
                            firebaseAuth.currentUser!!.displayName.toString(),
                            firebaseAuth.currentUser!!.photoUrl.toString())
                    }
                }).addOnFailureListener{
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }
}