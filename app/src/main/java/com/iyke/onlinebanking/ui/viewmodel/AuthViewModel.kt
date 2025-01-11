package com.iyke.onlinebanking.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.ProgressDialog
import com.iyke.onlinebanking.activities.MainActivity
import com.iyke.onlinebanking.activities.VerifyPhoneNumber
import com.iyke.onlinebanking.utils.Constants
import com.iyke.onlinebanking.utils.Constants.EMAIL
import com.iyke.onlinebanking.utils.Constants.NAME
import com.iyke.onlinebanking.utils.Constants.PREFERENCE
import com.iyke.onlinebanking.utils.Constants.PROFILE
import com.iyke.onlinebanking.utils.Constants.USERS


open class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginWithEmailAndPassword(email: String?, password: String, activity: Activity) {
        val callBox = ProgressDialog(activity)
        callBox.show()
        firebaseAuth.signInWithEmailAndPassword(email!!, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    saveUserDataWithSharedPreference(email, "null", "null")

                    Intent(context, MainActivity::class.java).let { e ->
                        e.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //kills previous activities
                        context.startActivity(e)
                    }
                    callBox.dismiss()
                } else {
                    Toast.makeText(
                        context.applicationContext,
                        "Login Failure: " + task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    callBox.dismiss()
                }
            })
    }

    fun registerWithEmailAndPassword(
        email: String?,
        password: String?,
        name: String,
        activity: Activity
    ) {
        val callBox = ProgressDialog(activity)
        callBox.show()
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    saveUserDataWithSharedPreference(email, name, "null")
                    Intent(context, VerifyPhoneNumber::class.java).let { e ->
                        e.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(e)
                    }
                    callBox.dismiss()
                } else {
                    callBox.dismiss()
                    Toast.makeText(
                        context.applicationContext,
                        "Registration Failure: " + task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun saveUserDataWithSharedPreference(email: String, name: String, profilePic: String) {

        context.getSharedPreferences(PREFERENCE, MODE_PRIVATE).let {
            val myEdit = it.edit()
            myEdit.putString(EMAIL, email)
            myEdit.putString(NAME, name)
            myEdit.putString(PROFILE, profilePic)
            myEdit.apply()
        }
    }

    fun firebaseLogin(idToken: String, activity: Activity) {
        val callBox = ProgressDialog(activity)
        callBox.show()
        GoogleAuthProvider.getCredential(idToken, null).let { it ->
            firebaseAuth.signInWithCredential(it)
                .addOnCompleteListener(OnCompleteListener { it ->
                    if (it.isSuccessful) {
                        FirebaseFirestore.getInstance().collection(USERS).document(firebaseAuth.currentUser!!.email.toString()).get()
                            .addOnSuccessListener { doc ->
                                if (!doc.exists()) {
                                    saveUserDataWithSharedPreference(
                                        firebaseAuth.currentUser!!.email.toString(),
                                        firebaseAuth.currentUser!!.displayName.toString(),
                                        firebaseAuth.currentUser!!.photoUrl.toString()
                                    )
                                    Intent(context, VerifyPhoneNumber::class.java).let { e ->
                                        e.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        context.startActivity(e)
                                    }
                                    callBox.dismiss()
                                } else {
                                    saveUserDataWithSharedPreference(
                                        firebaseAuth.currentUser!!.email.toString(),
                                        firebaseAuth.currentUser!!.displayName.toString(),
                                        firebaseAuth.currentUser!!.photoUrl.toString()
                                    )

                                    Intent(context, MainActivity::class.java).let { e ->
                                        e.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //kills previous activities
                                        context.startActivity(e)
                                    }
                                    callBox.dismiss()
                                }
                            }.addOnFailureListener {
                            Log.d(
                                "VerifyActivity",
                                "Log in failed because ${it.message}"
                            )
                        }

                    }
                }).addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG).show()
                    callBox.dismiss()
                }
        }
    }
}