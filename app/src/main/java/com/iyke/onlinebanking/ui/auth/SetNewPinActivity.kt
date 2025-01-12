package com.iyke.onlinebanking.ui.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.ProgressDialog
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.ActivitySetNewPinBinding
import com.iyke.onlinebanking.ui.utils.CheckInternet
import com.iyke.onlinebanking.ui.utils.Constants

class SetNewPinActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetNewPinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetNewPinBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sh: SharedPreferences = this.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val firebaseEmail = sh.getString(Constants.EMAIL, "")

        binding.buttonConfirmPin.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.buttonConfirmPin.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    binding.buttonConfirmPin.setBackgroundResource(R.drawable.button_bg_custom)

                    if (binding.setNewPin1.length() < 6) {
                        binding.setNewPin1.error = "insert 6 digit pin"
                        return@OnTouchListener true
                    }

                    if (binding.setNewPin2.length() < 6) {
                        binding.setNewPin2.error = "insert 6 digit pin"
                        return@OnTouchListener true
                    }

                    if (binding.setNewPin2.text.toString() != binding.setNewPin2.text.toString()) {
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
                        .update("pin", binding.setNewPin1.text.toString())
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
