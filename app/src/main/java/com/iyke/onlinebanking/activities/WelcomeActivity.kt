package com.iyke.onlinebanking.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iyke.onlinebanking.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        createAccount.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        login.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }
}