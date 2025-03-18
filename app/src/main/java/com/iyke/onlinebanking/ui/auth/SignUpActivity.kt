package com.iyke.onlinebanking.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.iyke.onlinebanking.MainActivity
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.ActivitySignUpBinding
import com.iyke.onlinebanking.ui.dialog.ProgressDialog
import com.iyke.onlinebanking.utils.Constants.RC_SIGN_IN
import com.iyke.onlinebanking.utils.NetworkResults
import com.iyke.onlinebanking.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var binding:ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.next.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val cpassword = binding.confirmPasswordInput.text.toString()

            if (email.isEmpty()) {
                binding.emailInput.error = "Please Enter Email"
                binding.emailInput.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailInput.error = "Please Enter Valid Email"
                binding.emailInput.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.passwordInput.error = "Please Enter Password"
                binding.passwordInput.requestFocus()
                return@setOnClickListener
            }
            if (cpassword.isEmpty()) {
                binding.passwordInput.error = "Please Confirm Your Password"
                binding.passwordInput.requestFocus()
                return@setOnClickListener
            }
            if (password != cpassword) {
                Toast.makeText(this, "Password is not matched", Toast.LENGTH_SHORT).show()
            }

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && cpassword.isNotEmpty()) {
                authViewModel.registerWithEmail(email, password, name)
            }
        }

//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(secrets.clientId)
//            .requestEmail()
//            .build()
//
//        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleLogin.setOnClickListener {
          //  startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        }

        val progressDialog = ProgressDialog(applicationContext)

        lifecycleScope.launch {
            authViewModel.authResponse.collectLatest { result ->
                when (result) {
                    is NetworkResults.Loading -> {
                        progressDialog.show()
                    }
                    is NetworkResults.Success -> {
                        progressDialog.hide()

                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    is NetworkResults.Error -> {
                        progressDialog.hide()

                        Toast.makeText(this@SignUpActivity, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!
            authViewModel.loginWithGoogle(account.idToken!!)
        }
    }
}