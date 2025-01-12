package com.iyke.onlinebanking.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.ActivityVerifyPhoneNumberBinding
import com.iyke.onlinebanking.utils.NetworkInformation

class VerifyPhoneNumber : AppCompatActivity() {
    lateinit var binding:ActivityVerifyPhoneNumberBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editTextPhoneRegister.setSelection(4)

        binding.buttonSendcode.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    binding.buttonSendcode.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    binding.buttonSendcode.setBackgroundResource(R.drawable.button_bg_custom)

                    //hide keyboard
                    val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

                    if(!NetworkInformation(this).checkNow())
                    {
                        return@OnTouchListener true
                    }


                    val phoneNumber: String = binding.editTextPhoneRegister.text.toString()
                    if (phoneNumber.length == 14)
                    {
                        val intent = Intent(this, VerifyActivity::class.java)
                        intent.putExtra("phoneNumber",phoneNumber)
                        startActivity(intent)
                    }
                    else
                    {
                       binding.editTextPhoneRegister.error = "Invalid phone number add your country code"
                        Log.d("RegistrationActivity","Invalid phone number!")
                    }

                }
            }
            return@OnTouchListener true
        }
    }

}
