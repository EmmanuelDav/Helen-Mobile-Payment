package com.iyke.onlinebanking.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentAddMoneyBinding
import com.iyke.onlinebanking.viewmodel.UserDataViewModel

class AddMoney : Fragment() {

    var userDataViewModel: UserDataViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: FragmentAddMoneyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_money, container, false)
        userDataViewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)
        v.exitAddM.setOnClickListener { findNavController().popBackStack() }
        v.model = userDataViewModel
        v.executePendingBindings()
        return v.root
    }
}