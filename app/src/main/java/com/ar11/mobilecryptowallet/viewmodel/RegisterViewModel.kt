package com.ar11.mobilecryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ar11.mobilecryptowallet.auth.AppAuth2
import com.ar11.mobilecryptowallet.model.UserModel2
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
    private val auth2: AppAuth2,
) : ViewModel() {


    private val _userRegister = SingleLiveEvent<Boolean>()

    val userRegister: LiveData<Boolean>
        get() = _userRegister


    fun usersRegister(login: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                val response = repository.userRegister(
                    UserModel2(email = login, password = password,
                name = name)
                )
                auth2.setAuth2(response.email, response.name,response.avatarUrl, response.token, response.admin)
                _userRegister.value = true
            } catch (e: Exception) {
                _userRegister.value = false
            }
        }
    }


}
