package com.iyke.onlinebanking.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iyke.onlinebanking.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))

        }
        signIn.setOnClickListener {
            startActivity(Intent(this,VerifyPhoneNumber::class.java))
        }
    }
}