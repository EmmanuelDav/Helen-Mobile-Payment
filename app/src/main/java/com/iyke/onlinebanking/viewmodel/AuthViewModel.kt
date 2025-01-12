package com.iyke.onlinebanking.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iyke.onlinebanking.data.local.entries.UsersEntity
import com.iyke.onlinebanking.repository.AuthRepository
import com.iyke.onlinebanking.utils.NetworkResults
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private var _authResponse = MutableStateFlow<NetworkResults<UsersEntity>>(NetworkResults.Idle)
    val authResponse : StateFlow<NetworkResults<UsersEntity>> get() = _authResponse


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

}