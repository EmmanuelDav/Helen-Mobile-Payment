package com.iyke.onlinebanking.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.ProgressDialog
import com.iyke.onlinebanking.utils.CheckInternet
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.utils.Constants
import kotlinx.android.synthetic.main.activity_set_new_pin.*

class SetNewPinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_new_pin)
        val sh: SharedPreferences = this.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val firebaseEmail = sh.getString(Constants.EMAIL, "")

        button_confirm_pin.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    button_confirm_pin.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    button_confirm_pin.setBackgroundResource(R.drawable.button_bg_custom)

                    if (set_new_pin_1.length() < 6) {
                        set_new_pin_1.error = "insert 6 digit pin"
                        return@OnTouchListener true
                    }

                    if (set_new_pin_2.length() < 6) {
                        set_new_pin_2.error = "insert 6 digit pin"
                        return@OnTouchListener true
                    }

                    if (set_new_pin_1.text.toString() != set_new_pin_2.text.toString()) {
                        Toast.makeText(this, "Pin doesn't match", Toast.LENGTH_SHORT).show()
                        return@OnTouchListener true
                    }

                    //hide keyboard
                    val inputManager: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        currentFocus?.windowToken,
                        InputMethodManager.SHOW_FORCED
                    )

                    if (!CheckInternet(this).checkNow()) {
                        return@OnTouchListener true
                    }

                    val progressDialog = ProgressDialog(this)
                    progressDialog.show()
                    val db = FirebaseFirestore.getInstance()
                    db.collection("users")
                        .document(firebaseEmail!!)
                        .update("pin", set_new_pin_1.text.toString())
                        .addOnSuccessListener {
                            finish()
                            Toast.makeText(this, "Pin setup successful", Toast.LENGTH_SHORT).show()
                            progressDialog.dismiss()

                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Pin setup failed. try again!", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("SettNewPinActivity :", "error ${it.message}")
                            finish()
                            progressDialog.dismiss()
                        }

                }
            }
            return@OnTouchListener true
        }

    }
}
