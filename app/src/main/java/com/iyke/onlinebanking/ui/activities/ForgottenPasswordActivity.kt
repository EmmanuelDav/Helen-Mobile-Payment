package com.iyke.onlinebanking.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.iyke.onlinebanking.R
import kotlinx.android.synthetic.main.activity_forgotten_password.*

class ForgottenPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotten_password)

        verify.setOnClickListener {
            val email: String = emailInp.text.toString().trim { it <= ' ' }

            if (email.isEmpty()){
                Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this, "Email sent Successfully", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}