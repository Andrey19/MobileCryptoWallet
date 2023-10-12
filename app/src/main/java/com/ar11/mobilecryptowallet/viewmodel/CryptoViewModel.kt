package com.ar11.mobilecryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.model.CryptosModelState
import com.ar11.mobilecryptowallet.repository.CryptoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class CryptoViewModel @Inject constructor(
    private val repository: CryptoRepository,
) : ViewModel() {

    val data: LiveData<List<Cryptos>> = repository.data.asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<CryptosModelState>()
    val dataState: LiveData<CryptosModelState>
        get() = _dataState

    init {
        loadCryptos()
    }

    fun loadCryptos() = viewModelScope.launch {
        try {
            _dataState.value = CryptosModelState(loading = true)
            repository.getAll()
            _dataState.value = CryptosModelState()
        } catch (e: Exception) {
            _dataState.value = CryptosModelState(error = true)
        }
    }

    fun getCryptoList(): List<String> {
        return data.value!!.map { it.cryptoName }
    }

    fun getCrypto(cryptoName: String):  Cryptos {
        return data.value!!.first{crypto ->  crypto.cryptoName == cryptoName}
    }

    fun getCryptoIndex(crypto: Cryptos): Int {
        return data.value!!.indexOf(crypto)
    }

    val updatedCryptos: LiveData<Unit> = data.switchMap {
        repository.getAllCryptosFlow()
            .catch { e -> e.printStackTrace() }
            .asLiveData(Dispatchers.Default)
    }

    fun refreshCryptos() = viewModelScope.launch {
        try {
            _dataState.value = CryptosModelState(refreshing = true)
            repository.getAll()
            _dataState.value = CryptosModelState()
        } catch (e: Exception) {
            _dataState.value = CryptosModelState(error = true)
        }
    }

}
