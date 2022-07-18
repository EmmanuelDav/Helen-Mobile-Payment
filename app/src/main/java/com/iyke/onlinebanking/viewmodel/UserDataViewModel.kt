package com.iyke.onlinebanking.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.Timestamp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.iyke.onlinebanking.utils.Constants.BALANCE
import com.iyke.onlinebanking.utils.Constants.STATEMENT
import com.iyke.onlinebanking.utils.Constants.USERS
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.StatementItem
import com.iyke.onlinebanking.databinding.FragmentSendMoneyBinding
import com.iyke.onlinebanking.intface.StatementInterface
import com.iyke.onlinebanking.intface.UserInterface
import com.iyke.onlinebanking.model.Statement
import com.iyke.onlinebanking.model.Users
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_statement_actitvity.*
import kotlin.random.Random


class UserDataViewModel(application: Application) : AndroidViewModel(application),UserInterface<Users>,StatementInterface<Statement> {

    private val context = getApplication<Application>().applicationContext

    var basicListener : UserInterface<Users> = this
    var statementlistener : StatementInterface<Statement> = this

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    val statements = MutableLiveData<ArrayList<Statement>>(ArrayList<Statement>())


    var userData = MutableLiveData<Users>()

    val users = MutableLiveData<ArrayList<Users>>(ArrayList<Users>())

    val amountAdded = MutableLiveData<Int>()
    val addMoney = MutableLiveData<String>()
    val message = MutableLiveData<String>()
    private  var clickedUser = Users()
    init {
        addMoney.value = ""
    }

    private fun addFunds(view: View){
        if (addMoney.value!!.isNotEmpty() ){
            val docRef = FirebaseFirestore.getInstance().collection(USERS).document(FirebaseAuth.getInstance().currentUser!!.email.toString())
            docRef.get().addOnSuccessListener { doc ->

                   docRef.update(BALANCE,doc[BALANCE].toString().toInt() + addMoney.value!!.toInt())
                    Log.d("VerifyActivity", "${addMoney.value!!.toInt()}  signInWithCredential:success")

                    val myStatementData = hashMapOf(
                        "amount" to addMoney,
                        "from" to "my Bank",
                        "time" to Timestamp.now()
                    )

                    val txId = "TID-SM-"+ Random.nextBytes(9)
                    db.collection(USERS).document(FirebaseAuth.getInstance().currentUser?.email.toString()).collection(STATEMENT).document(txId).set(myStatementData)
                        .addOnSuccessListener {
                            Toast.makeText(context, "$${addMoney.value} Added added", Toast.LENGTH_SHORT).show()
                            Navigation.findNavController(view).popBackStack()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context,"my statement update failed", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {   Log.d("VerifyActivity", "Log in failed because ${it.message}") }
        }else{
            Toast.makeText(context, "No Amount added", Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchUserDetails() {
        db.collection(USERS).document(FirebaseAuth.getInstance().currentUser?.email.toString())
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
        }
    }

    fun sendMooney(v:View){
        val myStatementData = hashMapOf(
            "amount" to amountAdded.value!!.toInt(),
            "client_email" to clickedUser.email,
            "from" to "me",
            "message" to message,
            "time" to Timestamp.now()
        )

        val txId = "TID-SM-"+ Random.nextBytes(9)
        db.collection(USERS).document(FirebaseAuth.getInstance().currentUser?.email.toString()).collection(STATEMENT).document(txId).set(myStatementData)
            .addOnSuccessListener {
                Toast.makeText(context,"Money was sent successfully", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(v).popBackStack(R.id.homeFragment,false)
            }
            .addOnFailureListener {
                Toast.makeText(context,"my statement update failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun fetchUsers(){
        val tempArr = ArrayList<Users>()
        db.collection(USERS).get().addOnSuccessListener { doc ->
            for(user in doc){
                val user = user.toObject(Users::class.java)
                tempArr.add(user)
            }
            users.value = tempArr

        }.addOnFailureListener { Log.d("VerifyActivity", "Log in failed because ${it.message}") }
    }

    override fun onItemClick(user: Users) {
        clickedUser = user
        //Navigation.findNavController(view).navigate(R.id.action_sentFragment_to_sendMoney2)
    }

     fun fetchStatement() {
        val statementArray = ArrayList<Statement>()
        db.collection(USERS).document(FirebaseAuth.getInstance().currentUser?.email.toString()).collection(STATEMENT).orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents)
                {
                    val statement = Statement(doc["amount"].toString(), doc["from"].toString(), doc["client_number"].toString(), doc["time"] as Timestamp)
                    statementArray.add(statement)
                }
                statements.value = statementArray
            }.addOnFailureListener {}
    }

    override fun onItemClick(statement: Statement) {

    }

}