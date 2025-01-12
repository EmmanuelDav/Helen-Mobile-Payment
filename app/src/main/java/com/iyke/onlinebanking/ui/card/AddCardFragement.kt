package com.iyke.onlinebanking.ui.card

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.databinding.FragmentAddCardFragemntBinding
import com.iyke.onlinebanking.ui.dialog.ProgressDialog
import com.iyke.onlinebanking.utils.Constants
import com.iyke.onlinebanking.utils.Constants.CARDS
import com.iyke.onlinebanking.viewmodel.StatisticsViewModel

class AddCardFragement : Fragment() {
    private var statisticsViewModel: StatisticsViewModel? = null
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private lateinit var binding: FragmentAddCardFragemntBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_card_fragemnt, container, false)
        statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
        binding.exitAddC.setOnClickListener {
            findNavController().popBackStack()
        }
        val sh: SharedPreferences = requireActivity().getSharedPreferences(Constants.PREFERENCE, AppCompatActivity.MODE_PRIVATE)
        val firebaseEmail = sh.getString(Constants.EMAIL, "")
        binding.addCard.setOnClickListener {
            val pcardName = binding.cardName.text.toString()
            val pcardLabel = binding.cardLabel.text.toString()
            val pcardNumber = binding.cardNumber.text.toString()
            val pcvv = binding.cvv.text.toString()
            val pcity = binding.city.text.toString()
            val pexpirtation = binding.cardDate.text.toString()
            val pzipCode = binding.zipCode.text.toString()

            if (pcardName.isEmpty()) {
                binding.cardName.error = "Please Enter cardName"
                binding.cardName.requestFocus()
                return@setOnClickListener
            }

            if (pcardLabel.isEmpty()) {
                binding.cardLabel.error = "Please Enter bank"
                binding.cardLabel.requestFocus()
                return@setOnClickListener
            }

            if (pcardNumber.isEmpty()) {
                binding.cardNumber.error = "Please Enter cardNumber"
                binding.cardNumber.requestFocus()
                return@setOnClickListener
            }
            if (pcvv.isEmpty()) {
                binding.cvv.error = "Please enter Your cvv "
                binding.cvv.requestFocus()
                return@setOnClickListener
            }

            if (pcity.isEmpty()) {
                binding.city.error = "Please enter your city "
                binding.city.requestFocus()
                return@setOnClickListener
            }
            if (pexpirtation.isEmpty()) {
                binding.cardDate.error = "Please enter card Date "
                binding.cardDate.requestFocus()
                return@setOnClickListener
            }

            if (pzipCode.isEmpty()) {
                binding.zipCode.error = "Please enter your city's zipcode "
                binding.zipCode.requestFocus()
                return@setOnClickListener
            }
            if (pcvv.isNotEmpty() && pcity.isNotEmpty() && pexpirtation.isNotEmpty() &&
                pcardLabel.isNotEmpty() && pcardName.isNotEmpty() && pzipCode.isNotEmpty() && pzipCode.isNotEmpty()){

                val progressDialog = ProgressDialog(requireView().context)
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
        return binding.root
    }
}