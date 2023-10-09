package com.ar11.mobilecryptowallet.viewmodel


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
class LoginViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val auth: AppAuth,
) : ViewModel() {

    private val _userLogin = SingleLiveEvent<Boolean>()

    val userLogin: LiveData<Boolean>
        get() = _userLogin


    fun usersLogin(login: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.userLogin(UserModel(email = login, password = password))
                auth.setAuth(response.email, response.name,response.avatarUrl, response.token)
                _userLogin.value = true
            } catch (e: Exception) {
                _userLogin.value = false
            }
        }
    }


}
