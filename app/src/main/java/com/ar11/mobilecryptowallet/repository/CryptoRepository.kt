package com.ar11.mobilecryptowallet.repository


import com.ar11.mobilecryptowallet.dto.CryptoInWalletRequest
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.dto.WalletsModel
import com.ar11.mobilecryptowallet.model.TokenModel
import com.ar11.mobilecryptowallet.model.UserModel
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    val data: Flow<List<Cryptos>>

    val walletsData: Flow<List<WalletsModel>>

    suspend fun getAllWallets(userId: String)

    suspend fun getAll()

    fun getAllCryptosFlow(): Flow<Unit>

    suspend fun userLogin(user: UserModel): TokenModel

    suspend fun userRegister(user: UserModel): TokenModel

    suspend fun saveWallet(wallet: WalletsModel)

    suspend fun updateWallet(wallet: WalletsModel)

    suspend fun deleteWallet(wallet: WalletsModel)

    suspend fun saveCryptoToWallet(cryptoToWallet: CryptoInWalletRequest)

    suspend fun updateCryptoInWallet(cryptoToWallet: CryptoInWalletRequest)

    suspend fun deleteCryptoInWallet(cryptoInWallet: CryptoInWalletRequest)

}

