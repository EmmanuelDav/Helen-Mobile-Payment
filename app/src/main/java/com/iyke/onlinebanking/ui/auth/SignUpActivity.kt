package com.iyke.onlinebanking.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.iyke.onlinebanking.utils.Constants.RC_SIGN_IN
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.auth.secrets
import com.iyke.onlinebanking.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {


    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        login.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        next.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val cpassword = confirmPasswordInput.text.toString()

            if (email.isEmpty()) {
                emailInput.error = "Please Enter Email"
                emailInput.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Please Enter Valid Email"
                emailInput.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordInput.error = "Please Enter Password"
                passwordInput.requestFocus()
                return@setOnClickListener
            }
            if (cpassword.isEmpty()) {
                passwordInput.error = "Please Confirm Your Password"
                passwordInput.requestFocus()
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