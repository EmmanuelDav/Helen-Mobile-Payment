package com.iyke.onlinebanking.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.data.local.dao.UsersDao
import com.iyke.onlinebanking.data.local.entries.UsersEntity
import com.iyke.onlinebanking.models.Users
import com.iyke.onlinebanking.utils.Constants.USERS
import com.iyke.onlinebanking.utils.NetworkResults
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val usersDao: UsersDao
) {

    suspend fun loginWithEmail(email: String, password: String): NetworkResults<UsersEntity> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val existingUser = usersDao.getExistingUser()
            val user = UsersEntity(email = email,
                name = existingUser?.name,
                profileUrl = existingUser?.profileUrl,
                balance = existingUser?.balance,
                phoneNumber = existingUser?.phoneNumber
            )
            NetworkResults.Success(user)
        } catch (e: Exception) {
            NetworkResults.Error(e.message!!)
        }
    }


    suspend fun registerWithEmail(
        email: String,
        password: String,
        name: String
    ): NetworkResults<UsersEntity> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val firestore = uploadUserDataToFireStore(email, name, "")

            val existingUser = usersDao.getExistingUser()
            val user = UsersEntity(email = email,
                name = existingUser?.name,
                profileUrl = existingUser?.profileUrl,
                balance = existingUser?.balance,
                phoneNumber = existingUser?.phoneNumber,
                userId = existingUser?.userId
            )

            if (firestore.isSuccess){
                usersDao.insertUsers(user)
                NetworkResults.Success(user)
            }else{
                throw Exception("Failed to upload user data to Firestore: ${firestore.exceptionOrNull()?.message}")
            }
        } catch (e: Exception) {
            NetworkResults.Error(e.message!!)
        }
    }


    suspend fun loginWithEmail(idToken: String): NetworkResults<UsersEntity> {
        return try {
            val credentials = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credentials)
            val firebaseUser = auth.currentUser ?: throw Exception("Google auth failed")
            val firestore = uploadUserDataToFireStore(firebaseUser.email!!, firebaseUser.displayName!!, firebaseUser.photoUrl.toString())
            val user = UsersEntity(
                email = firebaseUser.email,
                name = firebaseUser.displayName,
                profileUrl = firebaseUser.photoUrl.toString(),
                balance = "0.00",
                phoneNumber = "",
                userId = 0
            )
            if (firestore.isSuccess){
                usersDao.insertUsers(user)
                NetworkResults.Success(user)
            }else{
                throw Exception("Failed to upload user data to Firestore: ${firestore.exceptionOrNull()?.message}")
            }
        } catch (e: Exception) {
            NetworkResults.Error(e.message!!)
        }
    }

    suspend fun loginWithPhoneNumber(credential: PhoneAuthCredential): NetworkResults<UsersEntity> {
        return try {
            auth.signInWithCredential(credential)
            val firebaseUser = auth.currentUser ?: throw Exception("Google auth failed")
            val firestore = uploadUserDataToFireStore(firebaseUser.email!!, firebaseUser.displayName!!, firebaseUser.photoUrl.toString())

            val user = UsersEntity(
                email = firebaseUser.email,
                name = firebaseUser.displayName,
                profileUrl = firebaseUser.photoUrl.toString(),
                balance = "0.00",
                phoneNumber = "",
                userId = 0
            )
            if (firestore.isSuccess){
                usersDao.insertUsers(user)
                NetworkResults.Success(user)
            }else{
                throw Exception("Failed to upload user data to Firestore: ${firestore.exceptionOrNull()?.message}")
            }
        } catch (e: Exception) {
            NetworkResults.Error(e.message!!)
        }
    }


    private suspend fun uploadUserDataToFireStore(email: String, name: String, profileUrl: String): Result<String> {
        return try {
            val users = hashMapOf(
                "name" to name,
                "email" to email,
                "profileUrl" to profileUrl,
                "balance" to "0.00",
                "phoneNumber" to "",
                "balance" to "1000" // Note: You have two "balance" keys here.
            )
            val documentReference = firestore.collection(USERS).document(email)
            documentReference.set(users).await()
            Result.success(documentReference.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}