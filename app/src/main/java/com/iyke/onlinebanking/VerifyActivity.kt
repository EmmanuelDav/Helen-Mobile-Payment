package com.iyke.onlinebanking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_send_money_activity.*
import kotlinx.android.synthetic.main.activity_verify.*
import java.util.concurrent.TimeUnit

class VerifyActivity : AppCompatActivity() {

    private lateinit var storedVerificationId: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)


        auth = FirebaseAuth.getInstance()
        val phoneNumber: String = intent.getStringExtra("phoneNumber")
        sendVerificationCode(phoneNumber)

        button_verify.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    button_verify.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    button_verify.setBackgroundResource(R.drawable.button_bg_custom)

                    val code: String = editText_ver_code.text.toString().trim()
                    if (code.length < 6 || code.isEmpty()) {
                        editText_ver_code.error = "invalid code"
                        return@OnTouchListener true
                    }

                    //hide keyboard
                    val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

                    if(!CheckInternet(this).checkNow())
                    {
                        return@OnTouchListener true
                    }

                    verify_progressBar.visibility = View.VISIBLE
                    verifyCode(code)


                }
            }
            return@OnTouchListener true
        }

    }


    private fun sendVerificationCode(phoneNumber: String)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks) // OnVerificationStateChangedCallbacks
    }


    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("VerifyActivity", "onVerificationCompleted:$credential")

            val code = credential.smsCode
            editText_ver_code.setText(code)
            Log.d("VerifyActivity", "code complete:" + code.toString())
            verify_progressBar.visibility = View.VISIBLE
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("VerifyActivity", "onVerificationFailed", e)
            Toast.makeText(this@VerifyActivity,e.message,Toast.LENGTH_LONG).show()
            finish() //go back to registration activity

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        override fun onCodeSent(
            verificationId: String?,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("VerifyActivity", "onCodeSent:" + verificationId!!)

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            //resendToken = token

            // ...
        }
    }

    private fun verifyCode(code: String)
    {
        val credential:PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                       val docRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
                       docRef.get()
                           .addOnSuccessListener { doc ->
                               if(doc["balance"] == null)
                               {
                                   docRef.update("balance",1000)
                               }
                               if(doc["pin"] == null)
                               {
                                   intent = Intent(this,SetNewPinActivity::class.java)
                                   intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //kills previous activities
                                   startActivity(intent)
                                   Log.d("VerifyActivity", "signInWithCredential:success")
                               }
                               else
                               {
                                   intent = Intent(this,UserActivity::class.java)
                                   intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //kills previous activities
                                   startActivity(intent)
                                   Log.d("VerifyActivity", "signInWithCredential:success")
                               }

                           }
                           .addOnFailureListener {   Log.d("VerifyActivity", "Log in failed because ${it.message}") }


                    // Sign in success, update UI with the signed-in user's information
                } else {
                    // Sign in failed, display a message and update the UI
                    verify_progressBar.visibility = View.INVISIBLE
                    Log.w("VerifyActivity", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this@VerifyActivity,"signInWithCredential:failure",Toast.LENGTH_SHORT).show()
                    finish() //finish this activity and get back to registration activity
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this@VerifyActivity,"code entered was invalid",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}
