package com.iyke.onlinebanking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentSendMoneyBinding
import com.iyke.onlinebanking.viewmodel.UserDataViewModel

class SendMoney : Fragment() {

    lateinit var userDataViewModel: UserDataViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val v:FragmentSendMoneyBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_send_money, container, false)
        v.lifecycleOwner = this
        userDataViewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)
        v.exitSend.setOnClickListener { findNavController().popBackStack() }
        v.model = userDataViewModel
        v.executePendingBindings()
        v.model?.sendMooney()
        return v.root
    }
}