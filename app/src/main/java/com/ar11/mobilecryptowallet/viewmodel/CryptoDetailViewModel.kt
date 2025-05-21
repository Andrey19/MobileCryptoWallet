package com.ar11.mobilecryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ar11.mobilecryptowallet.auth.AppAuth2
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.repository.CryptoRepository
import com.ar11.mobilecryptowallet.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class CryptoDetailViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val auth2: AppAuth2,
) : ViewModel() {

    private val _cryptoData = SingleLiveEvent<Cryptos>()

    val cryptoData: LiveData<Cryptos>
        get() = _cryptoData

    fun saveCryptoInfo(crypto: Cryptos) = viewModelScope.launch {
        try {
            if (auth2.authStateFlow2.value.email != null) {
                val cryptoInfo = repository.saveCryptoInfo(crypto)
                _cryptoData.value = cryptoInfo
                println("------------------------- Криптовалюта сохранена успешно")
            }
        } catch (e: Exception) {
            println("------------------------- Ошибка при сохранении криптовалюты")
        }
    }


}
