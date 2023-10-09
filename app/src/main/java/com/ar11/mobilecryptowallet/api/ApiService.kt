package com.ar11.mobilecryptowallet.api

import com.ar11.mobilecryptowallet.BuildConfig
import com.ar11.mobilecryptowallet.dto.CryptoInWalletRequest
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.dto.WalletsModel
import com.ar11.mobilecryptowallet.model.TokenModel
import com.ar11.mobilecryptowallet.model.UserModel
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*



private const val BASE_URL = "${BuildConfig.BASE_URL}/api/"

fun okhttp(vararg interceptors: Interceptor): OkHttpClient = OkHttpClient.Builder()
    .apply {
        interceptors.forEach {
            this.addInterceptor(it)
        }
    }
    .build()

fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(client)
    .build()


interface ApiService {
    @GET("cryptos")
    suspend fun getAll(): Response<List<Cryptos>>

    @POST("user/mobile/login")
    suspend fun userLogin(@Body user: UserModel): Response<TokenModel>

    @POST("user/mobile/register")
    suspend fun userRegister(@Body user: UserModel): Response<TokenModel>
    @GET("wallets")
    suspend fun getWallets(@Query("email") email: String): Response<List<WalletsModel>>

    @POST("wallet")
    suspend fun saveWallet(@Body wallet: WalletsModel): Response<WalletsModel>

    @PUT("wallet")
    suspend fun updateWallet(@Body wallet: WalletsModel): Response<WalletsModel>

    @DELETE("wallet/mobile")
    suspend fun deleteWallet(@Query("email") userId: String, @Query("walletName") walletName: String): Response<WalletsModel>

    @POST("wallet/crypto")
    suspend fun saveCryptoToWallet(@Body cryptoToWallet: CryptoInWalletRequest): Response<WalletsModel>

    @PUT("wallet/crypto")
    suspend fun updateCryptoInWallet(@Body cryptoToWallet: CryptoInWalletRequest): Response<WalletsModel>

    @DELETE("wallet/crypto/mobile")
    suspend fun deleteCryptoInWallet(@Query("email") userId: String, @Query("walletName") walletName: String,
                                     @Query("cryptoName") cryptoName: String): Response<WalletsModel>
}


