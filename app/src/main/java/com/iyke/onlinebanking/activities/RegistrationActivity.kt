package com.iyke.onlinebanking.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.iyke.onlinebanking.CheckInternet
import com.iyke.onlinebanking.R
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        editText_phone_register.setSelection(4)

        button_sendcode.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    button_sendcode.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    button_sendcode.setBackgroundResource(R.drawable.button_bg_custom)

                    //hide keyboard
                    val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

                    if(!CheckInternet(this).checkNow())
                    {
                        return@OnTouchListener true
                    }


                    val phoneNumber: String = editText_phone_register.text.toString()
                    if (phoneNumber.length == 14)
                    {
                        val intent = Intent(this, VerifyActivity::class.java)
                        intent.putExtra("phoneNumber",phoneNumber)
                        startActivity(intent)
                    }
                    else
                    {
                        editText_phone_register.error = "Invalid phone number"
                        Log.d("RegistrationActivity","Invalid phone number!")
                    }

                }
            }
            return@OnTouchListener true
        }

    }

}
