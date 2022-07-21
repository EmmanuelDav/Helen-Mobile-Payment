package com.iyke.onlinebanking.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.iyke.onlinebanking.utils.Constants
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.auth.secrets
import com.iyke.onlinebanking.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.emailInput
import kotlinx.android.synthetic.main.activity_sign_in.passwordInput

class SignInActivity : AppCompatActivity() {

    lateinit var authViewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        signUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

        }
        signIn.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()


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


            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.loginWithEmailAndPassword(email, password, this)
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(secrets.clientId)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleLogin.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, Constants.RC_SIGN_IN)
        }
        forPassword.setOnClickListener {
            startActivity(Intent(this, ForgottenPasswordActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN && resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)!!
            if (GoogleSignIn.getLastSignedInAccount(this) == null) {
                authViewModel.firebaseLogin(account.idToken!!, this)
            } else {
                Intent(this, MainActivity::class.java).let { e ->
                    e.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    this.startActivity(e)
                }
            }
        }
    }
}