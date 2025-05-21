package com.ar11.mobilecryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ar11.mobilecryptowallet.auth.AppAuth2
import com.ar11.mobilecryptowallet.dto.CryptoInWalletRequest
import com.ar11.mobilecryptowallet.dto.WalletsModel
import com.ar11.mobilecryptowallet.model.WalletsModelState
import com.ar11.mobilecryptowallet.repository.CryptoRepository
import com.ar11.mobilecryptowallet.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private val empty = WalletsModel(
    userId = "",
    walletName = "",
    walletDescription = "",
    cryptosCount = 0.0,
    cryptosCost = 0.0,
    cryptocurrenciesList = null
)

private val emptyCrypto = CryptoInWalletRequest(
    userId = "",
    walletName = "",
    cryptoName = "",
    cryptoType = "",
    image = "",
    imageUrl = "",
    cryptoDescription = "",
    cryptoAmount = 0.0,
    cryptoCost = 0.0,
)

@ExperimentalCoroutinesApi
@HiltViewModel
class WalletsViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val auth2: AppAuth2,
) : ViewModel() {

    val data: LiveData<List<WalletsModel>> = auth2
        .authStateFlow2
        .flatMapLatest { user ->
            repository.walletsData
                .map { wallets ->
                    wallets.filter {
                        it.userId == user.email
                    }
                }

        }.asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<WalletsModelState>()
    val dataState: LiveData<WalletsModelState>
        get() = _dataState

    private val newOrEdit = MutableLiveData<WalletsModel>(empty)
    val newOrEditChange: LiveData<WalletsModel>
        get() = newOrEdit

    private val newOrEditCrypto = MutableLiveData(emptyCrypto)

    private val viewCrypto = MutableLiveData(emptyCrypto)


    private val _updatedWallet = SingleLiveEvent<Unit>()

    val updatedWallet: LiveData<Unit>
        get() = _updatedWallet

    private val _deletedCryptoInWallet = SingleLiveEvent<Unit>()

    val deletedCryptoInWallet: LiveData<Unit>
        get() = _deletedCryptoInWallet


    private val _addedWallet = SingleLiveEvent<Unit>()

    val addedWallet: LiveData<Unit>
        get() = _addedWallet

    private val _addedCryptoToWallet = SingleLiveEvent<Unit>()

    val addedCryptoToWallet: LiveData<Unit>
        get() = _addedCryptoToWallet

    private val _updatedCryptoInWallet = SingleLiveEvent<Unit>()

    val updatedCryptoInWallet: LiveData<Unit>
        get() = _updatedCryptoInWallet

    private val _deletedWallet = SingleLiveEvent<Unit>()

    val deletedWallet: LiveData<Unit>
        get() = _deletedWallet

    init {
        if (auth2.authStateFlow2.value.email != null) {
            loadWallets(auth2.authStateFlow2.value.email!!)
        }
    }


    fun setNewOrEdit(wallet: WalletsModel?) {
        if (wallet == null) {
            newOrEdit.value = empty
        } else {
            newOrEdit.value = wallet!!
        }
    }

    fun getNewOrEdit(): WalletsModel? {
        return newOrEdit.value
    }

    fun setNewOrEditCrypto(crypto: CryptoInWalletRequest?) {
        if (crypto == null) {
            newOrEditCrypto.value = emptyCrypto
        } else {
            newOrEditCrypto.value = crypto
        }
    }

    fun getWallet(walletName: String): WalletsModel {
        return data.value!!.first{wallet ->  wallet.walletName == walletName}
    }

    fun getNewOrEditCrypto(): CryptoInWalletRequest? {
        return newOrEditCrypto.value
    }

    fun setViewCrypto(crypto: CryptoInWalletRequest?) {
        if (crypto == null) {
            viewCrypto.value = emptyCrypto
        } else {
            viewCrypto.value = crypto
        }
    }

    fun getViewCrypto(): CryptoInWalletRequest? {
        return viewCrypto.value
    }

    fun saveWallet() {

        newOrEdit.value?.let {
            _dataState.value = WalletsModelState(adding = true)
            viewModelScope.launch {
                try {
                    repository.saveWallet(it)
                    _dataState.value = WalletsModelState()
                    _addedWallet.value = Unit
                } catch (e: Exception) {
                    _dataState.value = WalletsModelState(error = true)
                }
            }
        }
        newOrEdit.value = empty
    }

    fun saveCryptoToWallet() {

        newOrEditCrypto.value?.let {
            if (it.cryptoName.isNotEmpty()) {
                _dataState.value = WalletsModelState(addingCrypto = true)
                viewModelScope.launch {
                    try {
                        repository.saveCryptoToWallet(it)
                        _dataState.value = WalletsModelState()
                        _addedCryptoToWallet.value = Unit

                    } catch (e: Exception) {
                        _dataState.value = WalletsModelState(error = true)
                    }
                }
            } else {
                _dataState.value = WalletsModelState(error = true)
            }
            newOrEditCrypto.value = emptyCrypto
        }

    }

    fun updateCryptoInWallet() {

        newOrEditCrypto.value?.let {
            if (it.cryptoName.isNotEmpty()) {
                _dataState.value = WalletsModelState(updatingCrypto = true)
                viewModelScope.launch {
                    try {
                        repository.updateCryptoInWallet(it)
                        _dataState.value = WalletsModelState()
                        _updatedCryptoInWallet.value = Unit

                    } catch (e: Exception) {
                        _dataState.value = WalletsModelState(error = true)
                    }
                }
            } else {
                _dataState.value = WalletsModelState(error = true)
            }
            newOrEditCrypto.value = emptyCrypto
        }

    }

    fun updateNewOrEdit() {
        if (newOrEdit.value!!.walletName.isNotEmpty()) {
            val wallet = getWallet(newOrEdit.value!!.walletName)
            newOrEdit.value = newOrEdit.value!!.copy(
                cryptosCount = wallet.cryptosCount,
                cryptosCost = wallet.cryptosCost,
                cryptocurrenciesList = wallet.cryptocurrenciesList
            )
        }
    }

    fun updateWallet() {

        newOrEdit.value?.let {
            _dataState.value = WalletsModelState(updating = true)
            viewModelScope.launch {
                try {
                    repository.updateWallet(it)
                    _dataState.value = WalletsModelState()
                    _updatedWallet.value = Unit
                } catch (e: Exception) {
                    _dataState.value = WalletsModelState(error = true)
                }
            }
        }
        newOrEdit.value = empty
    }

    fun deleteWallet(wallet: WalletsModel) {

        _dataState.value = WalletsModelState(deleting = true)
        viewModelScope.launch {
            try {
                repository.deleteWallet(wallet)
                _dataState.value = WalletsModelState()
                _deletedWallet.value = Unit
            } catch (e: Exception) {
                _dataState.value = WalletsModelState(error = true)
            }
        }

    }

    fun deleteCryptoInWallet(cryptoInWallet: CryptoInWalletRequest) {

        _dataState.value = WalletsModelState(deletingCrypto = true)
        viewModelScope.launch {
            try {
                repository.deleteCryptoInWallet(cryptoInWallet)
                _dataState.value = WalletsModelState()
                _deletedCryptoInWallet.value = Unit
            } catch (e: Exception) {
                _dataState.value = WalletsModelState(error = true)
            }
        }

    }

    fun loadWallets(userId: String) = viewModelScope.launch {
        try {
            _dataState.value = WalletsModelState(loading = true)
            repository.getAllWallets(userId)
            _dataState.value = WalletsModelState()
        } catch (e: Exception) {
            _dataState.value = WalletsModelState(error = true)
        }
    }

    fun refreshWallets(userId: String) = viewModelScope.launch {
        try {
            _dataState.value = WalletsModelState(refreshing = true)
            repository.getAllWallets(userId)
            _dataState.value = WalletsModelState()
        } catch (e: Exception) {
            _dataState.value = WalletsModelState(true)
        }
    }

}
