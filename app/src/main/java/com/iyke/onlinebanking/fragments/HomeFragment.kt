package com.iyke.onlinebanking.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.Constants.BALANCE
import com.iyke.onlinebanking.Constants.NAME
import com.iyke.onlinebanking.Constants.USERS
import com.iyke.onlinebanking.ProgressDialog
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentHomeBinding
import com.iyke.onlinebanking.viewmodel.UserDataViewModel

class HomeFragment : Fragment() {

    var userDataViewModel: UserDataViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        
        userDataViewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)


        val callBox = ProgressDialog(requireActivity())
        callBox.show()
        val bundle = Bundle()
        bundle.putString("amount", "sendMoney")

        v.sendMoney.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addMoney,bundle)
        }
        v.addFunds.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addMoney)
        }
        v.linearLayoutCompat4.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_historyFragment)
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Log.d("TAG", "onCreate: user ${user.phoneNumber},,, ${user.email}")
        } else {
            Log.d("TAG", "onCreate: nul")
        }

        val docRef = FirebaseFirestore.getInstance().collection(USERS)
            .document(FirebaseAuth.getInstance().currentUser?.email.toString())
        docRef.get().addOnSuccessListener { doc ->
               v.userName.text = "Hello ${doc[NAME].toString()}"
                v.balance.text = "$${doc[BALANCE].toString()}"
            callBox.dismiss()

        }.addOnFailureListener { Log.d("VerifyActivity", "Log in failed because ${it.message}")
            callBox.dismiss()
        }

        return v.root
    }

}