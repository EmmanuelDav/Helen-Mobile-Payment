package com.example.onlinebanking


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_send_money_activity.*
import kotlinx.android.synthetic.main.dialog_confirm_pin.*

class ConfirmPinDialog(private val activity: Activity) : Dialog(activity) {

    var confirmed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_pin)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        setOnCancelListener {
            dismiss()
        }

        button_confirm_dialog_pin.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    button_confirm_dialog_pin.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    button_confirm_dialog_pin.setBackgroundResource(R.drawable.button_bg_custom)

                    if(editText_dialog_box_pin.length() < 6)
                    {
                        editText_dialog_box_pin.error = "insert 6 digit pin"
                        return@OnTouchListener true
                    }

                    if(!CheckInternet(activity).checkNow())
                    {
                        return@OnTouchListener true
                    }

                    progressBar_confirm_pin.visibility = View.VISIBLE

                    val db = FirebaseFirestore.getInstance()
                    val docRef = db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document != null)
                            {
                                Log.d("ConfirmPinActivity", "DocumentSnapshot data: ${document.data}")
                                if(document["pin"].toString() != editText_dialog_box_pin.text.toString())
                                {
                                    progressBar_confirm_pin.visibility = View.INVISIBLE
                                    editText_dialog_box_pin.error = "incorrect pin"
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
                            progressBar_confirm_pin.visibility = View.INVISIBLE
                            editText_dialog_box_pin.error = "failed"
                        }


                }
            }
            return@OnTouchListener true
        }



    }

}
