package com.iyke.onlinebanking.ui.dialog


import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.DialogConfirmPinBinding
import com.iyke.onlinebanking.utils.Constants
import com.iyke.onlinebanking.utils.NetworkInformation

class ConfirmPinDialog(private val activity: Context) : Dialog(activity) {

    var confirmed = false
    private lateinit var binding:DialogConfirmPinBinding


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogConfirmPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        setOnCancelListener {
            dismiss()
        }
        val sh: SharedPreferences = context.getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val firebaseEmail = sh.getString(Constants.EMAIL, "")
        binding.buttonConfirmDialogPin.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    binding.buttonConfirmDialogPin.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    binding.buttonConfirmDialogPin.setBackgroundResource(R.drawable.button_bg_custom)

                    if(binding.editTextDialogBoxPin.length() < 6)
                    {
                        binding.editTextDialogBoxPin.error = "insert 6 digit pin"
                        return@OnTouchListener true
                    }

                    if(!NetworkInformation(activity).checkNow())
                    {
                        return@OnTouchListener true
                    }

                    binding.progressBarConfirmPin.visibility = View.VISIBLE

                    val db = FirebaseFirestore.getInstance()
                    val docRef = db.collection("users").document(firebaseEmail!!)
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document != null)
                            {
                                Log.d("ConfirmPinActivity", "DocumentSnapshot data: ${document.data}")
                                if(document["pin"].toString() != binding.editTextDialogBoxPin.text.toString())
                                {
                                   binding. progressBarConfirmPin.visibility = View.INVISIBLE
                                    binding.editTextDialogBoxPin.error = "incorrect pin"
                                }
                                else
                                {
                                    //data matched
                                    confirmed = true
                                    dismiss()
                                }
                            }
                            else
                            {
                                Log.d("ConfirmPinActivity", "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("ConfirmPinActivity", "get failed with ", exception)
                            binding.progressBarConfirmPin.visibility = View.INVISIBLE
                         //   binding.progressBarConfirmPin.error = "failed"
                        }


                }
            }
            return@OnTouchListener true
        }



    }

}
