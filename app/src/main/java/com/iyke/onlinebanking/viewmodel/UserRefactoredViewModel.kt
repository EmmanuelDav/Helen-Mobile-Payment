package com.iyke.onlinebanking.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.iyke.onlinebanking.models.BankStatements
import com.iyke.onlinebanking.models.Users
import com.iyke.onlinebanking.repository.UserRepository
import com.iyke.onlinebanking.ui.bind.StatementInterface
import com.iyke.onlinebanking.ui.bind.UserInterface
import com.iyke.onlinebanking.ui.dialog.ProgressDialog
import com.iyke.onlinebanking.utils.Constants.AMOUNT
import com.iyke.onlinebanking.utils.Constants.CLIENT_NAME
import com.iyke.onlinebanking.utils.Constants.TIME
import com.iyke.onlinebanking.utils.Constants.TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
abstract class UserRefactoredViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val application: Application
) : AndroidViewModel(application), UserInterface<Users>, StatementInterface<BankStatements> {

    val userData = MutableLiveData<Users>()
    val statements = MutableLiveData<List<BankStatements>>()
    val users = MutableLiveData<List<Users>>()
    val message = MutableLiveData<String>()
    val addMoney = MutableLiveData<String>()

    init {
        addMoney.value = ""
    }

    fun fetchUserDetails() {
        viewModelScope.launch {
            val email = userRepo.getCurrentEmail()
            if (email != null) {
                userRepo.getUserDetails(
                    email,
                    onSuccess = { user ->
                        userData.value = user
                    },
                    onFailure = { exception ->
                        Log.e("UserDataViewModel", "Error fetching user details: ${exception.message}")
                    }
                )
            } else {
                Log.e("UserDataViewModel", "Email is null")
            }
        }

    }

    fun addFunds(view: View) {
        viewModelScope.launch {
            val email = userRepo.getCurrentEmail()
            val amount = addMoney.value?.toIntOrNull()

            if (email != null && amount != null && amount > 0) {
                val progressDialog = ProgressDialog(view.context)
                progressDialog.show()

                userRepo.getUserDetails(
                    email,
                    onSuccess = { user ->
                        val newBalance = user.balance + amount
                        userRepo.updateUserBalance(
                            email,
                            newBalance.toString(),
                            onSuccess = {
                                val txId = "TID-SM-" + Random.nextBytes(9).joinToString("")
                                val transactionData = mapOf(
                                    AMOUNT to amount,
                                    CLIENT_NAME to "My Bank",
                                    TIME to Timestamp.now(),
                                    TYPE to "Added"
                                )
                                userRepo.addTransaction(
                                    email,
                                    txId,
                                    transactionData,
                                    onSuccess = {
                                        Toast.makeText(
                                            view.context,
                                            "$$amount added successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Navigation.findNavController(view).popBackStack()
                                        progressDialog.dismiss()
                                    },
                                    onFailure = { exception ->
                                        Log.e("UserDataViewModel", "Transaction failed: ${exception.message}")
                                        progressDialog.dismiss()
                                    }
                                )
                            },
                            onFailure = { exception ->
                                Log.e("UserDataViewModel", "Balance update failed: ${exception.message}")
                                progressDialog.dismiss()
                            }
                        )
                    },
                    onFailure = { exception ->
                        Log.e("UserDataViewModel", "Error fetching user details: ${exception.message}")
                        progressDialog.dismiss()
                    }
                )
            } else {
                Toast.makeText(view.context, "Invalid amount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun fetchStatements() {
        viewModelScope.launch {
            val email = userRepo.getCurrentEmail()
            if (email != null) {
                userRepo.getStatements(
                    email,
                    onSuccess = { statementList ->
                        statements.value = statementList
                    },
                    onFailure = { exception ->
                        Log.e("UserDataViewModel", "Error fetching statements: ${exception.message}")
                    }
                )
            }
        }
    }
}
