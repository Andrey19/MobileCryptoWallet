package com.ar11.mobilecryptowallet.repository


import com.ar11.mobilecryptowallet.dto.CryptoInWalletRequest
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.dto.ImageModel
import com.ar11.mobilecryptowallet.dto.Project
import com.ar11.mobilecryptowallet.dto.WalletsModel
import com.ar11.mobilecryptowallet.model.TokenModel2
import com.ar11.mobilecryptowallet.model.UserModel2
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface CryptoRepository {

    val data: Flow<List<Cryptos>>

    val walletsData: Flow<List<WalletsModel>>

    val projectDataFromDb: Flow<List<Project>>

    suspend fun getAllWallets(userId: String)

    suspend fun getAllProject(): List<Project>

    suspend fun getUser(email: String): UserModel2

    suspend fun updateUserInfo(user: UserModel2): UserModel2

    suspend fun uploadImage(imageFile: MultipartBody.Part): ImageModel

    suspend fun saveCryptoInfo(crypto: Cryptos): Cryptos

    suspend fun getAll()

    suspend fun updatePrice()

    suspend fun userLogin2(user: UserModel2): TokenModel2

    fun getAllCryptosFlow(): Flow<Unit>

    suspend fun getAllProjectFromDb()

    suspend fun userRegister(user: UserModel2): TokenModel2

    suspend fun saveWallet(wallet: WalletsModel)

    suspend fun updateWallet(wallet: WalletsModel)

    suspend fun deleteWallet(wallet: WalletsModel)

    suspend fun saveCryptoToWallet(cryptoToWallet: CryptoInWalletRequest)

    suspend fun updateCryptoInWallet(cryptoToWallet: CryptoInWalletRequest)

    suspend fun deleteCryptoInWallet(cryptoInWallet: CryptoInWalletRequest)

}

