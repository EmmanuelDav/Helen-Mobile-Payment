package com.iyke.onlinebanking.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.iyke.onlinebanking.Constants
import com.iyke.onlinebanking.Constants.BALANCE
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentHomeBinding
import com.iyke.onlinebanking.viewmodel.ActivitiesViewModel
import com.iyke.onlinebanking.viewmodel.AuthViewModel

class HomeFragment : Fragment() {
   
    lateinit var activityViewModel: ActivitiesViewModel
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v:FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        activityViewModel = ViewModelProvider(this).get(ActivitiesViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        v.sendMoney.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addMoney)
        }
        v.addFunds.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addMoney)
        }
        v.linearLayoutCompat4.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_historyFragment)
        }
        activityViewModel.userRef.document(auth.currentUser!!.phoneNumber.toString()).get()
            .addOnSuccessListener { doc ->
                v.balance.text = doc[BALANCE].toString()
            }
            .addOnFailureListener { Log.d("VerifyActivity", "Log in failed because ${it.message}") }
        return v.root
    }

}