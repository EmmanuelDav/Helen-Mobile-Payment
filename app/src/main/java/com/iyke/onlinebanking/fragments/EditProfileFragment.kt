package com.iyke.onlinebanking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var v:FragmentEditProfileBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_edit_profile, container, false)
        v.exitEdit.setOnClickListener{ findNavController().popBackStack() }
        return v.root
    }

}