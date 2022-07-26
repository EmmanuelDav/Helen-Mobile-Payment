package com.iyke.onlinebanking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.textview.MaterialTextView
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentProfileBinding
import com.iyke.onlinebanking.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    var profileViewModel:ProfileViewModel?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val v:FragmentProfileBinding =DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)
        v.lifecycleOwner = this
        profileViewModel = activity?.let { ViewModelProvider(it).get(ProfileViewModel::class.java) }
        v.profModel = profileViewModel
        v.executePendingBindings()
        v.profModel?.fetchUserDetails()
        return v.root
    }
}