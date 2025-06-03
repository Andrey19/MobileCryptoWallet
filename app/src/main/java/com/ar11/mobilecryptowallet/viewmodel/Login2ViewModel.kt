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
class Login2ViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val auth2: AppAuth2,
): ViewModel() {
    private val _userLogin2 = SingleLiveEvent<Boolean>()

    val userLogin2: LiveData<Boolean>
        get() = _userLogin2


    fun usersLogin2(login: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.userLogin2(UserModel2(email = login, password = password))
                auth2.setAuth2(response.email, response.name,response.avatarUrl, response.token, response.admin)
                _userLogin2.value = true
            } catch (e: Exception) {
                _userLogin2.value = false
            }
        }
    }
}
