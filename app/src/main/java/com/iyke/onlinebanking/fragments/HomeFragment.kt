package com.iyke.onlinebanking.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentHomeBinding
import com.iyke.onlinebanking.viewmodel.UserDataViewModel

class HomeFragment : Fragment() {

    var userDataViewModel: UserDataViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        userDataViewModel = ViewModelProvider(this).get(UserDataViewModel::class.java)
        v.lifecycleOwner = this
        v.viewmodel = userDataViewModel
        v.executePendingBindings()
        v.viewmodel?.fetchUserDetails()
        return v.root
    }

}