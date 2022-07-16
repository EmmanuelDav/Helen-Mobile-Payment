package com.iyke.onlinebanking.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.Constants.USERS
import com.iyke.onlinebanking.R
import com.iyke.onlinebanking.intface.UserInterface
import com.iyke.onlinebanking.model.Users


class UserDataViewModel(application: Application) : AndroidViewModel(application),UserInterface<Users> {

    var basicListener : UserInterface<Users> = this

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    var userData = MutableLiveData<Users>()
    val users = MutableLiveData<ArrayList<Users>>(ArrayList<Users>())


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
        }
    }

//    fun recordActivity(v: FragmentAddMoneyBinding){
//        val myStatementData = hashMapOf(
//            "amount" to v.addMmoney.text.toString().toInt(),
//            "client_email" to editText_sm_client_number.text.toString(),
//            "from" to "my Bank",
//            "time" to Timestamp.now()
//        )
//        val txId = "TID-SM-"+ Random.nextBytes(9)
//        db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()).collection("statements").document(txId).set(myStatementData)
//            .addOnSuccessListener {
//            }
//            .addOnFailureListener {
//                Toast.makeText(context,"my statement update failed", Toast.LENGTH_SHORT).show()
//            }
//    }

    fun fetchUsers(){
        val tempArr = ArrayList<Users>()
        db.collection(USERS).get().addOnSuccessListener { doc ->
            for(user in doc){
                val user = user.toObject(Users::class.java)
                tempArr.add(user)
            }
            users.value = tempArr

        }.addOnFailureListener {
                Log.d("VerifyActivity", "Log in failed because ${it.message}")
            }
    }

    override fun onItemClick(user: Users) {
        Toast.makeText(getApplication(), "item added", Toast.LENGTH_SHORT).show()
    }
}