package com.iyke.onlinebanking.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.ActivityVerifyBinding
import com.iyke.onlinebanking.MainActivity
import com.iyke.onlinebanking.ui.dialog.ProgressDialog
import com.iyke.onlinebanking.utils.Constants.BALANCE
import com.iyke.onlinebanking.utils.Constants.EMAIL
import com.iyke.onlinebanking.utils.Constants.NAME
import com.iyke.onlinebanking.utils.Constants.PHONE_NUMBER
import com.iyke.onlinebanking.utils.Constants.PIN
import com.iyke.onlinebanking.utils.Constants.PREFERENCE
import com.iyke.onlinebanking.utils.Constants.PROFILE
import com.iyke.onlinebanking.utils.Constants.USERS
import com.iyke.onlinebanking.utils.NetworkResults
import com.iyke.onlinebanking.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class VerifyActivity : AppCompatActivity() {

    private lateinit var storedVerificationId: String
    private lateinit var binding:ActivityVerifyBinding
    private lateinit var viewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class]

        val phoneNumber: String = intent.getStringExtra("phoneNumber")!!
        sendVerificationCode(phoneNumber)
        binding.pinEditText.transformationMethod = PasswordTransformationMethod.getInstance()

        binding.pinEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 4) { // Replace 4 with your desired length
                    verifyCode(s.toString()) // Call your logic here
                }
            }
        })


        val progressDialog = ProgressDialog(applicationContext)

        lifecycleScope.launch {
            viewModel.authResponse.collectLatest { result ->
                when (result) {
                    is NetworkResults.Loading -> {
                        progressDialog.show()
                    }
                    is NetworkResults.Success -> {
                        progressDialog.hide()

                        val intent = Intent(this@VerifyActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    is NetworkResults.Error -> {
                        progressDialog.hide()

                        Toast.makeText(this@VerifyActivity, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
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
            viewModel.loginWithPhoneNumber(credential)
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
        viewModel.loginWithPhoneNumber(credential)
    }


}
