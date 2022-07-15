package com.iyke.onlinebanking.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.Constants
import com.iyke.onlinebanking.Constants.BALANCE
import com.iyke.onlinebanking.ProgressDialog
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentAddMoneyBinding
import kotlinx.android.synthetic.main.fragment_add_money.*

class AddMoney : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       val v: FragmentAddMoneyBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_money, container, false)
//        if (arguments!!.getString("amount") == "sendMoney"){
//            v.confirmAddMoney.text = "No"
//        }
        v.confirmAddMoney.setOnClickListener {
            val progressDialog = ProgressDialog(requireActivity())
            progressDialog.show()
            if (addMmoney.text.toString() != null){
                val docRef = FirebaseFirestore.getInstance().collection(Constants.USERS).document(FirebaseAuth.getInstance().currentUser!!.email.toString())
                docRef.get()
                    .addOnSuccessListener { doc ->
                        docRef.update(BALANCE,addMmoney.text.toString())

                        Log.d("VerifyActivity", "signInWithCredential:success")
                    }
                    .addOnFailureListener {   Log.d("VerifyActivity", "Log in failed because ${it.message}") }
            }else{
                Toast.makeText(context, "No Amount added", Toast.LENGTH_SHORT).show()
            }
        }
        return v.root
    }
}