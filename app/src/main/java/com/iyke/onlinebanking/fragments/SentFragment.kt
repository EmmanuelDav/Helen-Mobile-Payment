package com.iyke.onlinebanking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentHomeBinding
import com.iyke.onlinebanking.databinding.FragmentSentBinding

class SentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v:FragmentSentBinding = DataBindingUtil.inflate(inflater ,R.layout.fragment_sent, container, false)
        v.exitSend.setOnClickListener{ findNavController().popBackStack() }
        return v.root
    }

}