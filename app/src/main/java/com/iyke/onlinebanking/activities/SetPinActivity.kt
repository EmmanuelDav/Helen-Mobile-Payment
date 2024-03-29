package com.iyke.onlinebanking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.utils.CheckInternet
import com.iyke.onlinebanking.R
import kotlinx.android.synthetic.main.activity_setpin.*

class SetPinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setpin)
        val db = FirebaseFirestore.getInstance();


        if(FirebaseAuth.getInstance().currentUser != null)
        {
            editText_phone_login.setText(FirebaseAuth.getInstance().currentUser?.email.toString())
        }
        else
        {
            intent = Intent(this, VerifyPhoneNumber::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //kills previous activities
            startActivity(intent)
        }

        button_login.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    button_login.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    button_login.setBackgroundResource(R.drawable.button_bg_custom)

                    if(editText2_password.length() < 6)
                    {
                        editText2_password.error = "enter 6 digit pin"
                        return@OnTouchListener true
                    }

                    //hide keyboard
                    val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

                    if(!CheckInternet(this).checkNow())
                    {
                        return@OnTouchListener true
                    }

                   // progressBar_login.visibility = View.VISIBLE

                    val db = FirebaseFirestore.getInstance()
                    val docRef = db.collection("users").document(FirebaseAuth.getInstance().currentUser?.email.toString())
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document != null)
                            {
                                Log.d("MainActivity", "DocumentSnapshot data: ${document.data}")
                                if(document["pin"].toString() != editText2_password.text.toString())
                                {
                                   // progressBar_login.visibility = View.INVISIBLE
                                    editText2_password.error = "incorrect pin"
                                }
                                else
                                {
                                    finish()
                                }
                            }
                            else
                            {
                                Log.d("MainActivity", "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("MainActivity", "get failed with ", exception)
                           // progressBar_login.visibility = View.INVISIBLE
                            Toast.makeText(this,"Login Failed!",Toast.LENGTH_SHORT).show()
                        }
                }
            }
            return@OnTouchListener true
        }
    }
}
