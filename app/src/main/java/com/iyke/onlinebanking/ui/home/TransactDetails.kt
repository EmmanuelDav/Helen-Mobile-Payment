package com.iyke.onlinebanking.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentTransactBinding
import com.iyke.onlinebanking.models.BankStatements

class TransactFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v:FragmentTransactBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_transact, container, false)
        val mBundle: Bundle = requireArguments()
        val statement: BankStatements = mBundle.getParcelable<BankStatements>("statement")!!
        v.statement = statement
        return v.root
    }
}