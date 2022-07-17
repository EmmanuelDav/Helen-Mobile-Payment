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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.Constants
import com.iyke.onlinebanking.Constants.BALANCE
import com.iyke.onlinebanking.ProgressDialog
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentAddMoneyBinding
import com.iyke.onlinebanking.viewmodel.UserDataViewModel
import kotlinx.android.synthetic.main.activity_send_money_activity.*
import kotlinx.android.synthetic.main.fragment_add_money.*
import kotlin.random.Random

class AddMoney : Fragment() {

    var userDataViewModel: UserDataViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: FragmentAddMoneyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_money, container, false)
        v.lifecycleOwner = this
        userDataViewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)
        v.exitAddM.setOnClickListener { findNavController().popBackStack() }
        v.model = userDataViewModel
        v.executePendingBindings()
        return v.root
    }
}