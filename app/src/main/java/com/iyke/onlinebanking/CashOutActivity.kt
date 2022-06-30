package com.iyke.onlinebanking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_cash_out.*
import kotlinx.android.synthetic.main.activity_send_money_activity.*
import kotlin.random.Random

class CashOutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_out)

        editText_co_client_number.setSelection(4)


        button_co_qr.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    button_co_qr.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    button_co_qr.setBackgroundResource(R.drawable.button_bg_custom)

                    //initiate barcode scanner
                    IntentIntegrator(this).initiateScan()


                }
            }
            return@OnTouchListener true
        }

        button_co_proceed.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    button_co_proceed.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    button_co_proceed.setBackgroundResource(R.drawable.button_bg_custom)

                    //hide keyboard
                    val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

                    if(!CheckInternet(this).checkNow())
                    {
                        return@OnTouchListener true
                    }

                    if (editText_co_client_number.length() < 14)
                    {
                        editText_co_client_number.error = "invalid phone number"
                        editText_co_client_number.requestFocus()
                        return@OnTouchListener true
                    }

                    if(editText_co_enter_amount.length() == 0)
                    {
                        editText_co_enter_amount.error = "enter amount"
                        return@OnTouchListener true
                    }

                    verifyAmount()

                }
            }
            return@OnTouchListener true
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null)
        {
            if(result.contents == null)
            {
                Toast.makeText(this,"barcode scanning cancelled", Toast.LENGTH_SHORT).show()
            }
            else
            {
               // editText_co_client_number.setPrefix("")
                editText_co_client_number.setText(result.contents.toString())
                Toast.makeText(this,"scan complete", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            //the camera will not closed if the result is not found
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun verifyAmount()
    {
        //check amount
        progressBar_co_proceed.visibility = View.VISIBLE
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document["balance"] != null)
                {
                    Log.d("SendMoneyActivity", "DocumentSnapshot data: ${document.data}")
                    if(document["balance"].toString().toInt() >= editText_co_enter_amount.text.toString().toInt())
                    {
                        progressBar_co_proceed.visibility = View.INVISIBLE
                        Toast.makeText(this,"Possible",Toast.LENGTH_SHORT).show()

                        //call security check
                        val callBox = ConfirmPinDialog(this)
                        callBox.show()
                        callBox.setOnDismissListener {
                            if(callBox.confirmed)
                            {
                                cashOut(document["balance"].toString().toInt())
                            }
                        }
                        //security check finish

                    }
                    if(document["balance"].toString().toInt() < editText_co_enter_amount.text.toString().toInt())
                    {
                        progressBar_co_proceed.visibility = View.INVISIBLE
                        editText_co_enter_amount.error = "insufficient balance"

                    }

                }
                else
                {
                    Log.d("SendMoneyActivity", "No such document")
                    progressBar_co_proceed.visibility = View.INVISIBLE
                    Toast.makeText(this,"documents not found",Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exception ->
                Log.d("SendMoneyActivity", "get failed with ", exception)
                progressBar_co_proceed.visibility = View.INVISIBLE
                Toast.makeText(this,"Balance checking failed",Toast.LENGTH_SHORT).show()
            }

    }

    private fun cashOut(myBalance: Int)
    {
        progressBar_co_proceed.visibility = View.VISIBLE
        //get client balance start
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("agents").document(editText_co_client_number.text.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document["balance"] != null)
                {
                    //update client balance
                    db.collection("agents").document(editText_co_client_number.text.toString()).update("balance", document["balance"].toString().toInt()+ editText_co_enter_amount.text.toString().toInt())
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {
                            Toast.makeText(this,"client balance update failed",Toast.LENGTH_SHORT).show()
                        }
                    //update client balance finish



                    //update my balance
                    db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()).update("balance", myBalance - editText_co_enter_amount.text.toString().toInt())
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {
                            Toast.makeText(this,"my balance update failed",Toast.LENGTH_SHORT).show()
                        }
                    //update my balance finish

                    //update my statement
                    val myStatementData = hashMapOf(
                        "amount" to editText_co_enter_amount.text.toString().toInt(),
                        "client_number" to editText_co_client_number.text.toString(),
                        "from" to "me",
                        "time" to Timestamp.now()
                    )
                    val txId = "TID-CO-"+ Random.nextBytes(9)
                    db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()).collection("statements").document(txId).set(myStatementData)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {
                            Toast.makeText(this,"my statement update failed",Toast.LENGTH_SHORT).show()
                        }
                    //finish update my statement

                    //update client statement
                    val clientStatementData = hashMapOf(
                        "amount" to editText_co_enter_amount.text.toString().toInt(),
                        "client_number" to FirebaseAuth.getInstance().currentUser?.phoneNumber.toString(),
                        "from" to "client",
                        "time" to Timestamp.now()
                    )
                    db.collection("agents").document(editText_co_client_number.text.toString()).collection("statements").document(txId).set(clientStatementData)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {
                            Toast.makeText(this,"client statement update failed",Toast.LENGTH_SHORT).show()
                        }
                    //finish client my statement

                    //transaction successful

                    Toast.makeText(this,"Cash Out successful",Toast.LENGTH_SHORT).show()
                    progressBar_co_proceed.visibility = View.INVISIBLE
                    finish()



                }
                else
                {
                    Log.d("CashOutActivity", "No such document")
                    progressBar_co_proceed.visibility = View.INVISIBLE
                    Toast.makeText(this,"agent not found",Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exception ->
                Log.d("CashOutActivity", "get failed with ", exception)
                progressBar_co_proceed.visibility = View.INVISIBLE
                Toast.makeText(this,"Transaction failed, internet error, couldn't get client balance",Toast.LENGTH_SHORT).show()
            }
        //get client balance end
    }

}
