package com.iyke.onlinebanking.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentConnectionBinding

class ConnectionFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v:FragmentConnectionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_connection, container, false)
        v.exitCon.setOnClickListener{ findNavController().popBackStack() }
        return v.root
    }
}