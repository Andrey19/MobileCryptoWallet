package com.ar11.mobilecryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ar11.mobilecryptowallet.model.CryptosModelState
import com.ar11.mobilecryptowallet.model.UserInfoModelState
import com.ar11.mobilecryptowallet.model.UserModel2
import com.ar11.mobilecryptowallet.repository.CryptoRepository
import com.ar11.mobilecryptowallet.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class ListUserInfoViewModel @Inject constructor(
    private val repository: CryptoRepository,
) : ViewModel() {

    private val _usersListData = SingleLiveEvent<List<UserModel2>>()

    val usersListData: LiveData<List<UserModel2>>
        get() = _usersListData

    private val _dataState = MutableLiveData<UserInfoModelState>()
    val dataState: LiveData<UserInfoModelState>
        get() = _dataState

    init {
        loadUsersInfo()
    }

    fun loadUsersInfo() = viewModelScope.launch {
        try {
            _dataState.value = UserInfoModelState(loading = true)
                val usersListInfo = repository.getUsersList()
                _usersListData.value = usersListInfo
            _dataState.value = UserInfoModelState()
        } catch (e: Exception) {
            println("------------------------- Ошибка при приеме пакета с пользовательской информацией")
            _dataState.value = UserInfoModelState(error = true)
        }
    }

    fun refreshUsers() = viewModelScope.launch {
        try {
            _dataState.value = UserInfoModelState(refreshing = true)
            val usersListInfo = repository.getUsersList()
            _usersListData.value = usersListInfo
            _dataState.value = UserInfoModelState()
        } catch (e: Exception) {
            _dataState.value = UserInfoModelState(error = true)
        }
    }

}