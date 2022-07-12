package com.iyke.onlinebanking.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iyke.onlinebanking.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.activity_welcome.login

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        login.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
        next.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }

    }
}