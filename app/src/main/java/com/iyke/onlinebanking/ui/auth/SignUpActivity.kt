package com.iyke.onlinebanking.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.ActivitySignUpBinding
import com.iyke.onlinebanking.ui.viewmodel.AuthViewModel

class SignUpActivity : AppCompatActivity() {


    lateinit var authViewModel: AuthViewModel
    private lateinit var binding:ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

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
                authViewModel.registerWithEmailAndPassword(email, password, name, this)
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(secrets.clientId)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleLogin.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!
            authViewModel.firebaseLogin(account.idToken!!, this)
        }
    }
}