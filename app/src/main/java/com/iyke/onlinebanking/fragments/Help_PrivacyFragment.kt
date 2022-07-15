package com.iyke.onlinebanking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentHelpPrivacyBinding

class Help_PrivacyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v:FragmentHelpPrivacyBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_help__privacy, container, false)
        v.exitPriv.setOnClickListener{ findNavController().popBackStack() }
        return v.root
    }

}