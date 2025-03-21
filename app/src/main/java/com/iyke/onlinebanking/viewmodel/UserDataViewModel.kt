package com.iyke.onlinebanking.viewmodel

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Timestamp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.iyke.onlinebanking.ui.dialog.ConfirmPinDialog
import com.iyke.onlinebanking.ui.dialog.ProgressDialog
import com.iyke.onlinebanking.utils.Constants.BALANCE
import com.iyke.onlinebanking.utils.Constants.STATEMENT
import com.iyke.onlinebanking.utils.Constants.USERS
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.models.BankStatements
import com.iyke.onlinebanking.models.Users
import com.iyke.onlinebanking.ui.auth.SetNewPinActivity
import com.iyke.onlinebanking.ui.bind.StatementInterface
import com.iyke.onlinebanking.ui.bind.UserInterface
import com.iyke.onlinebanking.utils.Constants
import com.iyke.onlinebanking.utils.Constants.AMOUNT
import com.iyke.onlinebanking.utils.Constants.CLIENT_NAME
import com.iyke.onlinebanking.utils.Constants.MESSAGE
import com.iyke.onlinebanking.utils.Constants.PIN
import com.iyke.onlinebanking.utils.Constants.TIME
import com.iyke.onlinebanking.utils.Constants.TYPE
import kotlin.random.Random


class UserDataViewModel(application: Application) : AndroidViewModel(application),
    UserInterface<Users>, StatementInterface<BankStatements> {


    private val context = getApplication<Application>().applicationContext
    var basicListener: UserInterface<Users> = this
    var statementlistener: StatementInterface<BankStatements> = this
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val statements = MutableLiveData(ArrayList<BankStatements>())
    var userData = MutableLiveData<Users?>()
    val users = MutableLiveData(ArrayList<Users>())
    lateinit var homeFragment: View
    lateinit var sendFragment: View
    val amountAdded = MutableLiveData<String>()
    val addMoney = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    private lateinit var clickedUser: Users
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    val firebaseEmail = firebaseUser?.email  // User's email
    val displayName = firebaseUser?.displayName

    init {
        addMoney.value = ""
        amountAdded.value = ""
        message.value=""
    }


    private fun addFunds(view: View) {
        if (addMoney.value!!.isNotEmpty()) {
            val progressDialog = ProgressDialog(view.context)
            progressDialog.show()
            val docRef = FirebaseFirestore.getInstance().collection(USERS)
                .document(firebaseEmail!!)
            docRef.get().addOnSuccessListener { doc ->

                docRef.update(BALANCE, doc[BALANCE].toString().toInt() + addMoney.value!!.toInt())
                Log.d("VerifyActivity", "${addMoney.value!!.toInt()}  signInWithCredential:success")

                val myStatementData = hashMapOf(
                    AMOUNT to addMoney.value!!.toInt(),
                    CLIENT_NAME to "my Bank",
                    TIME to Timestamp.now(),
                    TYPE to "Added"
                )

                val txId = "TID-SM-" + Random.nextBytes(9)
                db.collection(USERS)
                    .document(firebaseEmail)
                    .collection(STATEMENT).document(txId).set(myStatementData)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "$${addMoney.value} Added added",
                            Toast.LENGTH_SHORT
                        ).show()
                        Navigation.findNavController(view).popBackStack()
                        progressDialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "my statement update failed", Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                    }
            }
                .addOnFailureListener {
                    Log.d(
                        "VerifyActivity",
                        "Log in failed because ${it.message}"
                    )
                }
        } else {
            Toast.makeText(context, "No Amount added", Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchUserDetails() {
        db.collection(USERS).document(firebaseEmail!!)
            .get().addOnSuccessListener { doc ->
                val user = doc.toObject(Users::class.java)
                userData.value = user
            }.addOnFailureListener {
                Log.d("VerifyActivity", "Log in failed because ${it.message}")
            }
    }

    fun navigateView(view: View) {
        when (view.id) {
            R.id.addFunds -> Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_addMoney)
            R.id.sendMoney -> Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_sentFragment)
            R.id.see -> Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_historyFragment)
            R.id.confirmAddMoney -> addFunds(view)
            R.id.confirmSend -> verifyAmount(view)
        }
    }

    private fun verifyAmount(view: View) {
        val db = FirebaseFirestore.getInstance()
        val docRef =
            db.collection(USERS).document(firebaseEmail!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document[BALANCE] != null) {
                    if (document[BALANCE].toString().toInt() >= amountAdded.value!!.toInt()) {
                        Toast.makeText(context, "Possible", Toast.LENGTH_SHORT).show()

                        if (document[PIN] != null){
                            val callBox = ConfirmPinDialog(view.context)
                            callBox.show()
                            callBox.setOnDismissListener {
                                if(callBox.confirmed)
                                {
                                    sendMoney(document["balance"].toString().toInt(), view)
                                }
                            }

                        } else{
                            view.context.startActivity(Intent(context, SetNewPinActivity::class.java))
                        }
                    }
                    if (document[BALANCE].toString().toInt() < amountAdded.value!!.toInt()) {
                        Toast.makeText(context, "insufficient fund", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Log.d("SendMoneyActivity", "No such document")
                    Toast.makeText(context, "documents not found", Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exception ->
                Log.d("SendMoneyActivity", "get failed with ", exception)
                Toast.makeText(context, "Balance checking failed", Toast.LENGTH_SHORT).show()
            }

    }

    private fun sendMoney(myBalance: Int, view: View) {
        val progressDialog = ProgressDialog(view.context)
        progressDialog.show()

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(USERS).document(clickedUser.email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document[BALANCE] != null) {
                    db.collection(USERS).document(clickedUser.email)
                        .update(BALANCE, document[BALANCE].toString().toInt() + amountAdded.value!!.toInt()
                    )
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "client balance update failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    db.collection(USERS)
                        .document(firebaseEmail!!)
                        .update("balance", myBalance - amountAdded.value!!.toInt())
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "my balance update failed", Toast.LENGTH_SHORT)
                                .show()
                        }

                    val myStatementData = hashMapOf(
                        AMOUNT to +amountAdded.value.toString().toInt(),
                        CLIENT_NAME to "to "+clickedUser.name,
                        TIME to Timestamp.now(),
                        MESSAGE to message.value.toString(),
                       TYPE to "Debited"
                    )
                    val txId = "TID-SM-" + Random.nextBytes(9)
                    db.collection(USERS)
                        .document(firebaseEmail)
                        .collection(
                            STATEMENT
                        ).document(txId).set(myStatementData)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "my statement update failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    val clientStatementData = hashMapOf(
                        AMOUNT to amountAdded.value.toString().toInt(),
                        CLIENT_NAME to "from $displayName",
                        TIME to Timestamp.now(),
                        MESSAGE to message.value.toString(),
                        TYPE to "Credited"

                    )
                    db.collection(USERS).document(clickedUser.email).collection(STATEMENT)
                        .document(txId).set(clientStatementData)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "client statement update failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    Toast.makeText(context, "Transaction successful", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                    Navigation.findNavController(view).popBackStack(R.id.homeFragment, false)
                } else {
                    Log.d("SendMoneyActivity", "No such document")
                    Toast.makeText(context, "recipient not found", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("SendMoneyActivity", "get failed with ", exception)
                Toast.makeText(
                    context,
                    "Transaction failed, internet error, couldn't get client balance",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    fun fetchUsers(view: View) {
        sendFragment = view
        val tempArr = ArrayList<Users>()
        db.collection(USERS).get().addOnSuccessListener { doc ->
            for (user in doc) {
                    val user = user.toObject(Users::class.java)
                if (user.email != firebaseEmail){
                    tempArr.add(user)
                }
            }
            users.value = tempArr

        }.addOnFailureListener { Log.d("VerifyActivity", "Log in failed because ${it.message}") }
    }

    override fun onItemClick(user: Users) {
        val bundle = Bundle()
        bundle.putParcelable("User", user)
        Navigation.findNavController(sendFragment)
            .navigate(R.id.action_sentFragment_to_sendMoney2, bundle)
    }

    fun fetchStatement(fragment: View) {
        val progressDialog = ProgressDialog(fragment.context)
        progressDialog.show()
        homeFragment = fragment
        val statementArray = ArrayList<BankStatements>()
        db.collection(USERS).document(firebaseEmail!!)
            .collection(STATEMENT).orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val statement = BankStatements(
                        doc[AMOUNT].toString(),
                        doc[TYPE].toString(),
                        doc[CLIENT_NAME].toString(),
                        doc[TIME] as Timestamp,
                        doc[MESSAGE].toString()
                    )
                    statementArray.add(statement)
                }
                statements.value = statementArray
                progressDialog.dismiss()
            }.addOnFailureListener {}
    }

    override fun onItemClick(statement: BankStatements) {
        val bundle = Bundle()
        bundle.putParcelable("statement", statement)
        Navigation.findNavController(homeFragment)
            .navigate(R.id.action_homeFragment_to_transactFragment,bundle)
    }

    fun getUsers(user: Users) {
        clickedUser = user
    }
}