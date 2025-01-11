package com.iyke.onlinebanking.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentAddCardFragemntBinding
import com.iyke.onlinebanking.utils.Constants
import com.iyke.onlinebanking.utils.Constants.CARDS
import com.iyke.onlinebanking.viewmodel.StatisticsViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_add_card_fragemnt.*

class AddCardFragement : Fragment() {

    private var statisticsViewModel: StatisticsViewModel? = null
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val v: FragmentAddCardFragemntBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_card_fragemnt, container, false)
        statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
        v.exitAddC.setOnClickListener {
            findNavController().popBackStack()
        }
        val sh: SharedPreferences = requireActivity().getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val firebaseEmail = sh.getString(Constants.EMAIL, "")
        v.addCard.setOnClickListener {
            val pcardName = cardName.text.toString()
            val pcardLabel = cardLabel.text.toString()
            val pcardNumber = cardNumber.text.toString()
            val pcvv = cvv.text.toString()
            val pcity = city.text.toString()
            val pexpirtation = cardDate.text.toString()
            val pzipCode = zipCode.text.toString()

            if (pcardName.isEmpty()) {
                cardName.error = "Please Enter cardName"
                cardName.requestFocus()
                return@setOnClickListener
            }

            if (pcardLabel.isEmpty()) {
                cardLabel.error = "Please Enter bank"
                cardLabel.requestFocus()
                return@setOnClickListener
            }

            if (pcardNumber.isEmpty()) {
                cardNumber.error = "Please Enter cardNumber"
                cardNumber.requestFocus()
                return@setOnClickListener
            }
            if (pcvv.isEmpty()) {
                cvv.error = "Please enter Your cvv "
                cvv.requestFocus()
                return@setOnClickListener
            }

            if (pcity.isEmpty()) {
                city.error = "Please enter your city "
                city.requestFocus()
                return@setOnClickListener
            }
            if (pexpirtation.isEmpty()) {
                cardDate.error = "Please enter card Date "
                cardDate.requestFocus()
                return@setOnClickListener
            }

            if (pzipCode.isEmpty()) {
                zipCode.error = "Please enter your city's zipcode "
                zipCode.requestFocus()
                return@setOnClickListener
            }
            if (pcvv.isNotEmpty() && pcity.isNotEmpty() && pexpirtation.isNotEmpty() &&
                pcardLabel.isNotEmpty() && pcardName.isNotEmpty() && pzipCode.isNotEmpty() && pzipCode.isNotEmpty()){

                val progressDialog = com.iyke.onlinebanking.ProgressDialog(requireView().context)
                progressDialog.show()

                val cardDetails = hashMapOf(
                    "CardLabel" to pcardLabel,
                    "CardName" to pcardName,
                    "CardNumber" to pcardNumber,
                    "ExpireDate" to pexpirtation,
                    "CVC" to pcvv,
                    "ZipCode" to pzipCode,
                    "City" to pcity
                )

                db.collection(Constants.USERS).document(firebaseEmail!!).collection(CARDS)
                    .add(cardDetails as Map<String, Any>)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(context, "updated successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()

                    }.addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(context, "failed to update user", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
        return v.root
    }
}