package com.example.onlinebanking

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.AuthResult
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeoutException


class MyDatabase {



    fun checkBalance(myCallback: MyCallback)
    {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document["balance"] != null)
                {
                    myCallback.onCallback(document["balance"].toString().toInt())

                }
                else
                {
                    Log.d("SendMoneyActivity", "No such document")

                }
            }
            .addOnFailureListener {
                Log.d("MyFirestore", "get failed with ", it)
            }

    }

    fun getBalance(): Int{
        var balance= 0
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(FirebaseAuth.getInstance().currentUser?.phoneNumber.toString())
        val task: Task<DocumentSnapshot> = docRef.get()

        try {
            // Block on the task for a maximum of 500 milliseconds, otherwise time out.
            val snap: DocumentSnapshot = Tasks.await(task)
            balance=snap["balance"].toString().toInt()
        } catch (e: ExecutionException) {
            // ...
        } catch (e: InterruptedException) {
            // ...
        } catch (e: TimeoutException) {
            // Task timed out before it could complete.
            // ...
        }

        return balance
    }


}

interface MyCallback {
    fun onCallback(value: Int)
}