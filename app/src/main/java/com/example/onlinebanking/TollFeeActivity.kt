package com.example.onlinebanking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_send_money_activity.*
import kotlinx.android.synthetic.main.activity_toll_fee.*
import kotlinx.android.synthetic.main.activity_toll_fee.progressBar_sm_proceed
import kotlin.random.Random

class TollFeeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toll_fee)


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            R.layout.spinner_select_vehicle
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner_tf_select_vehicle_type.adapter = adapter
        }
        spinner_tf_select_vehicle_type.setSelection(0)

        //search toll booth start
        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line)


        val db = FirebaseFirestore.getInstance()
        db.collection("toll_booth")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                {
                    adapter.add(document.id)
                }
            }
            .addOnFailureListener {
                Log.d("TollFeeActivity","toll_booths_search failed")
                Toast.makeText(this,"Search failed",Toast.LENGTH_SHORT).show()
            }

        editText_tf_toll_booth.setAdapter(adapter)

        editText_tf_toll_booth.setOnDismissListener {
            //hide keyboard
            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
        }


        button_tf_qr.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    button_tf_qr.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    button_tf_qr.setBackgroundResource(R.drawable.button_bg_custom)

                    //initiate barcode scanner
                    IntentIntegrator(this).initiateScan()

                }
            }
            return@OnTouchListener true
        }


        button_tf_proceed.setOnTouchListener OnTouchListener@{ v, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    button_tf_proceed.setBackgroundResource(R.drawable.icon_menu_bg_custom_2)
                }
                MotionEvent.ACTION_UP -> {
                    button_tf_proceed.setBackgroundResource(R.drawable.button_bg_custom)
                    if(!CheckInternet(this).checkNow())
                    {
                        return@OnTouchListener true
                    }

                    //check for valid toll booth name start
                    db.collection("toll_booth")
                        .get()
                        .addOnSuccessListener { documents ->
                            var found = false
                            for (document in documents)
                            {
                                if(document.id == editText_tf_toll_booth.text.toString())
                                {
                                    found = true
                                    //toll booth name is valid
                                    if(editText_tf_enter_reg_no.text.length < 10)
                                    {
                                        editText_tf_enter_reg_no.error = "invalid registration number"
                                        return@addOnSuccessListener
                                    }
                                    else
                                    {
                                        //valid registration number

                                        //generate toll fee start
                                        db.collection("toll_booth").document(editText_tf_toll_booth.text.toString())
                                            .get()
                                            .addOnSuccessListener { document ->
                                                if (document[spinner_tf_select_vehicle_type.selectedItem.toString()] != null)
                                                {
                                                    val fee = document[spinner_tf_select_vehicle_type.selectedItem.toString()].toString().toInt()
                                                    //checking balance
                                                    val balanceRef = db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
                                                    balanceRef.get()
                                                        .addOnSuccessListener {document ->
                                                            if(document["balance"].toString().toInt() < fee)
                                                            {
                                                                Toast.makeText(this,"insufficient balance!\nfee for you ${spinner_tf_select_vehicle_type.selectedItem} is $fee taka",Toast.LENGTH_LONG).show()
                                                                return@addOnSuccessListener
                                                            }
                                                            else
                                                            {

                                                                //call security check
                                                                val callBox = ConfirmPinDialog(this)
                                                                callBox.show()
                                                                callBox.setOnDismissListener {
                                                                    if(callBox.confirmed)
                                                                    {
                                                                        //update my balance
                                                                        db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()).update("balance", document["balance"].toString().toInt() - fee)
                                                                            .addOnSuccessListener {
                                                                            }
                                                                            .addOnFailureListener {
                                                                                Toast.makeText(this,"my balance update failed",Toast.LENGTH_SHORT).show()
                                                                            }
                                                                        //update my balance finish

                                                                        //update my statement
                                                                        val myStatementData = hashMapOf(
                                                                            "amount" to fee,
                                                                            "client_number" to editText_tf_toll_booth.text.toString(),
                                                                            "from" to "me",
                                                                            "time" to Timestamp.now()
                                                                        )
                                                                        val txId  = "TID-TF-"+ Random.nextBytes(9)
                                                                        db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()).collection("statements").document(txId).set(myStatementData)
                                                                            .addOnSuccessListener {
                                                                            }
                                                                            .addOnFailureListener {
                                                                                Toast.makeText(this,"my statement update failed",Toast.LENGTH_SHORT).show()
                                                                            }
                                                                        //finish update my statement

                                                                        //update client balance
                                                                        db.collection("toll_booth").document(editText_tf_toll_booth.text.toString())
                                                                            .get()
                                                                            .addOnSuccessListener { document ->
                                                                                db.collection("toll_booth").document(editText_tf_toll_booth.text.toString()).update("balance", document["balance"].toString().toInt()+ fee)
                                                                                    .addOnSuccessListener {

                                                                                    }
                                                                                    .addOnFailureListener {
                                                                                        Toast.makeText(this,"client balance update failed",Toast.LENGTH_SHORT).show()
                                                                                    }

                                                                            }
                                                                            .addOnFailureListener {  }

                                                                        //update client balance finish

                                                                        //update client statement
                                                                        val clientStatementData = hashMapOf(
                                                                            "amount" to fee,
                                                                            "client_number" to FirebaseAuth.getInstance().currentUser?.phoneNumber.toString(),
                                                                            "from" to "client",
                                                                            "vehicle_type" to spinner_tf_select_vehicle_type.selectedItem.toString(),
                                                                            "regNo" to editText_tf_enter_reg_no.text.toString(),
                                                                            "time" to Timestamp.now()
                                                                        )
                                                                        db.collection("toll_booth").document(editText_tf_toll_booth.text.toString()).collection("statements").document(txId).set(clientStatementData)
                                                                            .addOnSuccessListener {
                                                                            }
                                                                            .addOnFailureListener {
                                                                                Toast.makeText(this,"client statement update failed",Toast.LENGTH_SHORT).show()
                                                                            }
                                                                        //finish client my statement
                                                                        Toast.makeText(this,"Toll paid successfully",Toast.LENGTH_SHORT).show()
                                                                        finish()
                                                                    }
                                                                }
                                                                //security check finish


                                                                return@addOnSuccessListener
                                                            }
                                                        }
                                                        .addOnFailureListener {
                                                            Toast.makeText(this,"failed! try again",Toast.LENGTH_SHORT).show()
                                                            finish()
                                                        }
                                                    //checking balance end
                                                }
                                                else
                                                {
                                                    Log.d("TollFeeActivity", "vehicle type not found")
                                                }


                                            }
                                            .addOnFailureListener {
                                                Log.d("TollFeeActivity", "get failed with ", it)
                                                Toast.makeText(this,"failed ! try again",Toast.LENGTH_SHORT).show()
                                                finish()
                                            }
                                        //generate toll fee end


                                    }
                                }
                            }
                            if(!found) { editText_tf_toll_booth.error = "invalid toll booth name" }
                            else return@addOnSuccessListener
                        }
                        .addOnFailureListener {
                            Log.d("TollFeeActivity","toll_booths_check failed")
                            Toast.makeText(this,"toll booth name checking failed",Toast.LENGTH_SHORT).show()
                        }
                    //check for valid toll booth name end




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
                Toast.makeText(this,"barcode scanning cancelled",Toast.LENGTH_SHORT).show()
            }
            else
            {
                editText_tf_toll_booth.setText(result.contents.toString())
                Toast.makeText(this,"scan complete",Toast.LENGTH_SHORT).show()
                editText_tf_toll_booth.dismissDropDown()
            }
        }
        else
        {
            //the camera will not closed if the result is not found
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}
