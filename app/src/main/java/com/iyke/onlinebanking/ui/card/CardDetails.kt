package com.iyke.onlinebanking.ui.card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentCardDetailsBinding
import com.iyke.onlinebanking.models.CardInfo

class CardDetails : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v :FragmentCardDetailsBinding = DataBindingUtil.inflate(inflater ,R.layout.fragment_card_details, container, false)
        val mBundle: Bundle = requireArguments()
        val user: CardInfo = mBundle.getParcelable<CardInfo>("Cards")!!
        v.cards = user
        return v.root
    }

}