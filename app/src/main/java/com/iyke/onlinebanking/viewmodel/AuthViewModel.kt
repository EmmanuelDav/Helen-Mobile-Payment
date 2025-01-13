package com.iyke.onlinebanking.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.iyke.onlinebanking.data.local.entries.UsersEntity
import com.iyke.onlinebanking.repository.AuthRepository
import com.iyke.onlinebanking.utils.Constants.USERS
import com.iyke.onlinebanking.utils.NetworkResults
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthViewModel @Inject constructor(private val authRepository: AuthRepository, val firestore: FirebaseFirestore) : ViewModel() {

    private var _authResponse = MutableStateFlow<NetworkResults<UsersEntity>>(NetworkResults.Idle)
    val authResponse : StateFlow<NetworkResults<UsersEntity>> get() = _authResponse

    val _result = MutableLiveData<String>()
    var results:MutableLiveData<String> = _result


    fun loginWithEmail(email:String, password:String){
        viewModelScope.launch {
            _authResponse.value = NetworkResults.Loading
            _authResponse.value = authRepository.loginWithEmail(email, password)
        }
    }

    fun registerWithEmail(email: String, password: String, name: String) {
        viewModelScope.launch {
            _authResponse.value = NetworkResults.Loading
            _authResponse.value = authRepository.registerWithEmail(email, password, name)
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authResponse.value = NetworkResults.Loading
            _authResponse.value = authRepository.loginWithEmail(idToken)
        }
    }

    fun loginWithPhoneNumber(credential: PhoneAuthCredential){
        viewModelScope.launch {
            _authResponse.value = NetworkResults.Loading
            _authResponse.value = authRepository.loginWithPhoneNumber(credential)
        }
    }


    suspend fun updatePin(email: String, pin: String): String {
        return try {
            val userDoc: DocumentReference = firestore.collection(USERS).document(email)
            val updateTask: Task<Void> = userDoc.update("Pin", pin)
            updateTask.await() // Suspend execution until the update completes
            "Uploaded Successfully"
        } catch (e: Exception) {
            "Failed: ${e.message}"
        }
    }

}