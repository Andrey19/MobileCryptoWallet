package com.ar11.mobilecryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ar11.mobilecryptowallet.auth.AppAuth2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@HiltViewModel
class Auth2ViewModel @Inject constructor(private val auth2: AppAuth2): ViewModel()  {

    val data2: LiveData<AppAuth2.AuthState2> = auth2.authStateFlow2
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = auth2.authStateFlow2.value.email != null

}