package com.iyke.onlinebanking.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.iyke.onlinebanking.data.local.dao.UsersDao
import com.iyke.onlinebanking.models.BankStatements
import com.iyke.onlinebanking.models.Users
import com.iyke.onlinebanking.utils.Constants.AMOUNT
import com.iyke.onlinebanking.utils.Constants.CLIENT_NAME
import com.iyke.onlinebanking.utils.Constants.MESSAGE
import com.iyke.onlinebanking.utils.Constants.STATEMENT
import com.iyke.onlinebanking.utils.Constants.TIME
import com.iyke.onlinebanking.utils.Constants.TYPE
import com.iyke.onlinebanking.utils.Constants.USERS
import com.iyke.onlinebanking.utils.Constants.BALANCE
import java.sql.Statement
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val usersDao: UsersDao
) {

    fun getUserDetails(email: String, onSuccess: (Users) -> Unit, onFailure: (Exception) -> Unit) {
        firebaseFirestore.collection(USERS).document(email)
            .get()
            .addOnSuccessListener { doc ->
                val user = doc.toObject(Users::class.java)
                if (user != null) onSuccess(user)
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun updateUserBalance(
        email: String,
        newBalance: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseFirestore.collection(USERS).document(email)
            .update(BALANCE, newBalance)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun addTransaction(
        email: String,
        txId: String,
        transactionData: Map<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseFirestore.collection(USERS)
            .document(email)
            .collection(STATEMENT)
            .document(txId)
            .set(transactionData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getStatements(
        email: String,
        onSuccess: (List<BankStatements>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseFirestore.collection(USERS).document(email)
            .collection(STATEMENT)
            .orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { docs ->
                val statements = docs.mapNotNull { doc ->
                    BankStatements(
                        doc[AMOUNT].toString(),
                        doc[TYPE].toString(),
                        doc[CLIENT_NAME].toString(),
                        doc[TIME] as Timestamp,
                        doc[MESSAGE]?.toString() ?: ""
                    )
                }
                onSuccess(statements)
            }
            .addOnFailureListener { onFailure(it) }
    }

    suspend fun getCurrentEmail(): String? {
        return usersDao.getExistingUser()?.email
    }

    suspend fun getDisplayName(id: String): String? {
        return usersDao.getUserByID(id).name
    }
}
