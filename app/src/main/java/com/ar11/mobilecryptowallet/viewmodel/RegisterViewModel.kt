package com.ar11.mobilecryptowallet.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ar11.mobilecryptowallet.auth.AppAuth
import com.ar11.mobilecryptowallet.model.UserModel
import com.ar11.mobilecryptowallet.repository.CryptoRepository
import com.ar11.mobilecryptowallet.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val auth: AppAuth,
) : ViewModel() {


    private val _userRegister = SingleLiveEvent<Boolean>()

    val userRegister: LiveData<Boolean>
        get() = _userRegister


    fun usersRegister(login: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                val response = repository.userRegister(UserModel(email = login, password = password,
                name = name))
                auth.setAuth(response.email, response.name,response.avatarUrl, response.token)
                _userRegister.value = true
            } catch (e: Exception) {
                _userRegister.value = false
            }
        }
    }


}
