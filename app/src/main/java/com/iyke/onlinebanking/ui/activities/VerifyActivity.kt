package com.iyke.onlinebanking.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.andrognito.pinlockview.IndicatorDots
import com.andrognito.pinlockview.PinLockListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.utils.CheckInternet
import com.iyke.onlinebanking.utils.Constants.BALANCE
import com.iyke.onlinebanking.utils.Constants.EMAIL
import com.iyke.onlinebanking.utils.Constants.NAME
import com.iyke.onlinebanking.utils.Constants.PHONE_NUMBER
import com.iyke.onlinebanking.utils.Constants.PIN
import com.iyke.onlinebanking.utils.Constants.PREFERENCE
import com.iyke.onlinebanking.utils.Constants.PROFILE
import com.iyke.onlinebanking.utils.Constants.USERS
import com.iyke.onlinebanking.R
import kotlinx.android.synthetic.main.activity_verify.*
import java.util.concurrent.TimeUnit


class VerifyActivity : AppCompatActivity() {

    private lateinit var storedVerificationId: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        auth = FirebaseAuth.getInstance()
        val phoneNumber: String = intent.getStringExtra("phoneNumber")!!

        pinCodeView.setPinLockListener(mPinLockListener)
        pinCodeView.attachIndicatorDots(indicatorDots)
        pinCodeView.pinLength = 5
        pinCodeView.textColor = ContextCompat.getColor(this, R.color.black)
        indicatorDots.indicatorType = IndicatorDots.IndicatorType.FILL_WITH_ANIMATION
        sendVerificationCode(phoneNumber)
    }

    private val mPinLockListener: PinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

            if(!CheckInternet(applicationContext).checkNow())
            {

            }
            verifyCode(pin)
        }

        override fun onEmpty() {
            Log.d("debugger", "Pin empty")
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {
            Log.d(
                "debugger",
                "Pin changed, new length $pinLength with intermediate pin $intermediatePin"
            )
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
           // editText_ver_code.setText(code)
            Log.d("VerifyActivity", "code complete:" + code.toString())
          //  verify_progressBar.visibility = View.VISIBLE
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

        override fun onCodeSent(p0: String, token: PhoneAuthProvider.ForceResendingToken) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("VerifyActivity", "onCodeSent:" + p0)

            // Save verification ID and resending token so we can use them later
            storedVerificationId = p0
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
                    val sh = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
                    val email = sh.getString(EMAIL, "")
                    val name = sh.getString(NAME, "")
                    val profilePic = sh.getString(PROFILE, "")


                    val data = hashMapOf(
                        EMAIL to email,
                        NAME to name,
                        PROFILE to profilePic,
                        PHONE_NUMBER to auth.currentUser!!.phoneNumber,
                        BALANCE to null,
                        PIN to null
                    )

                    FirebaseFirestore.getInstance().collection(USERS).document(email!!)
                        .set(data)
                        .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

                    val docRef = FirebaseFirestore.getInstance().collection(USERS).document(email)
                       docRef.get()
                           .addOnSuccessListener { doc ->
                               if(doc[BALANCE] == null)
                               {
                                   docRef.update(BALANCE,1000)
                               }
                               intent = Intent(this, MainActivity::class.java)
                               intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //kills previous activities
                               startActivity(intent)
                               Log.d("VerifyActivity", "signInWithCredential:success")
                           }
                           .addOnFailureListener {   Log.d("VerifyActivity", "Log in failed because ${it.message}") }


                    // Sign in success, update UI with the signed-in user's information
                } else {
                    // Sign in failed, display a message and update the UI
                   // verify_progressBar.visibility = View.INVISIBLE
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
