package com.ar11.mobilecryptowallet.repository

import com.ar11.mobilecryptowallet.api.ApiService
import com.ar11.mobilecryptowallet.dao.CryptoDao
import com.ar11.mobilecryptowallet.dao.ProjectDao
import com.ar11.mobilecryptowallet.dao.WalletsDao
import com.ar11.mobilecryptowallet.dto.CryptoInWalletRequest
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.dto.ImageModel
import com.ar11.mobilecryptowallet.dto.Project
import com.ar11.mobilecryptowallet.dto.WalletsModel
import com.ar11.mobilecryptowallet.entity.CryptoEntity
import com.ar11.mobilecryptowallet.entity.ProjectEntity
import com.ar11.mobilecryptowallet.entity.WalletsEntity
import com.ar11.mobilecryptowallet.entity.toDto
import com.ar11.mobilecryptowallet.entity.toEntity
import com.ar11.mobilecryptowallet.error.ApiError
import com.ar11.mobilecryptowallet.error.AppError
import com.ar11.mobilecryptowallet.error.NetworkError
import com.ar11.mobilecryptowallet.error.UnknownError
import com.ar11.mobilecryptowallet.model.TokenModel2
import com.ar11.mobilecryptowallet.model.UserModel2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoRepositoryImpl @Inject constructor(
    private val cryptosDao: CryptoDao,
    private val walletsDao: WalletsDao,
    private val apiService: ApiService,
    private val projectDao: ProjectDao,
) : CryptoRepository {
    override val data = cryptosDao.getAll()
        .map(List<CryptoEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override val projectDataFromDb = projectDao.getAllProjectFromDb()
        .map(List<ProjectEntity>::toDto)
        .flowOn(Dispatchers.Default)



    override suspend fun updatePrice() {
        try {
            val response = apiService.updatePrice()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())

            cryptosDao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun userLogin2(user: UserModel2): TokenModel2 {
        try {
            val response = apiService.userLogin2(user)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: Exception) {
            throw UnknownError
        }
    }


    override suspend fun getAllProjectFromDb() {
        try {
            val response = apiService.getAllProject()
            if (!response.isSuccessful) {
                println("-------------mistake------------------------")
                println(response.message())
                throw ApiError(response.code(), response.message())
            }
            println("-------------okey------------------------")
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            println(body)

            projectDao.insert(body.toEntity())

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {

            throw UnknownError
        }
    }


    override suspend fun getAllProject(): List<Project> {
        try {
            val response = apiService.getAllProject()
            if (!response.isSuccessful) {
                println("-------------mistake------------------------")
                println(response.message())
                throw ApiError(response.code(), response.message())
            }
            println("-------------okey------------------------")
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            println(body)
            return body

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {

            throw UnknownError
        }
    }


    override suspend fun getAll() {
        try {
            val response = apiService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())

            cryptosDao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override val walletsData = walletsDao.getAll()
        .map(List<WalletsEntity>::toDto)
        .flowOn(Dispatchers.Default)


    override suspend fun getAllWallets(userId: String) {
        try {
            val response = apiService.getWallets(userId)
            println("-------------hear------------------------")
            if (!response.isSuccessful) {
                println("-------------mistake------------------------")
                println(response.message())
                throw ApiError(response.code(), response.message())
            }
            println("-------------okey------------------------")
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            println(body)

            walletsDao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {

            throw UnknownError
        }
    }

    override fun getAllCryptosFlow(): Flow<Unit> = flow {
        //while (true) {

        delay(10_000L)

        val response = apiService.getAll()
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }

        val body = response.body() ?: throw ApiError(response.code(), response.message())
        if(body.isNotEmpty()) {
            cryptosDao.insert(body.toEntity())
        }
        emit(Unit)
        //}
    }
        .catch { e -> throw AppError.from(e) }
        .flowOn(Dispatchers.Default)


    override suspend fun saveWallet(wallet: WalletsModel) {
        try {
            val response = apiService.saveWallet(wallet)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            walletsDao.insert(WalletsEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun updateWallet(wallet: WalletsModel) {
        try {
            val response = apiService.updateWallet(wallet)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            walletsDao.insert(WalletsEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun deleteWallet(wallet: WalletsModel) {
        try {
            //val response = apiService.deleteWallet(wallet.userId, wallet.walletName)
            val response = apiService.deleteWallet(wallet.userId, wallet.walletName)

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            walletsDao.deleteWallet(wallet.userId, wallet.walletName)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveCryptoToWallet(cryptoToWallet: CryptoInWalletRequest) {
        try {
            val response = apiService.saveCryptoToWallet(cryptoToWallet)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            walletsDao.insert(WalletsEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun updateCryptoInWallet(cryptoToWallet: CryptoInWalletRequest) {
        try {
            val response = apiService.updateCryptoInWallet(cryptoToWallet)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            walletsDao.insert(WalletsEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun deleteCryptoInWallet(cryptoToWallet: CryptoInWalletRequest) {
        try {
            val response = apiService.deleteCryptoInWallet(cryptoToWallet.userId,
                cryptoToWallet.walletName, cryptoToWallet.cryptoName)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            walletsDao.insert(WalletsEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveCryptoInfo(crypto: Cryptos): Cryptos {
        try {
            val response = apiService.saveCryptoInfo(crypto)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            // Записываем принятые данные в локальную базу
            cryptosDao.insert(CryptoEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }


    override suspend fun getUser(email: String): UserModel2 {
        try {
            val response = apiService.getUser(email)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())

            return body
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun uploadImage(imageFile: MultipartBody.Part): ImageModel {
        try {
            val response = apiService.uploadAvatar(imageFile)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())

            return body
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }


    override suspend fun updateUserInfo(user: UserModel2): UserModel2 {
        try {
            val response = apiService.updateUserInfo(user)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())

            return body
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }




    override suspend fun userRegister(user: UserModel2): TokenModel2 {
        try {
            val response = apiService.userRegister(user)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            return response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: Exception) {
            throw UnknownError
        }


    }

}

