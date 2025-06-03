package com.ar11.mobilecryptowallet.api


import com.ar11.mobilecryptowallet.BuildConfig
import com.ar11.mobilecryptowallet.dto.CryptoInWalletRequest
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.dto.ImageModel
import com.ar11.mobilecryptowallet.dto.Project
import com.ar11.mobilecryptowallet.dto.WalletsModel
import com.ar11.mobilecryptowallet.model.TokenModel2
import com.ar11.mobilecryptowallet.model.UserModel2
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


private const val BASE_URL = "${BuildConfig.BASE_URL}/api/"

fun okhttp(vararg interceptors: Interceptor): OkHttpClient = OkHttpClient.Builder()
    .readTimeout(20, TimeUnit.SECONDS)
    .connectTimeout(20,TimeUnit.SECONDS)
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

    @GET("projects")
    suspend fun getAllProject(): Response<List<Project>>

    @GET("user")
    suspend fun getUser(@Query("email") email: String): Response<UserModel2>

    @POST("crypto/mobile")
    suspend fun saveCryptoInfo(@Body crypto: Cryptos): Response<Cryptos>

    @POST("cryptos")
    suspend fun updatePrice(): Response<List<Cryptos>>

    @POST("user/mobile/login")
    suspend fun userLogin2(@Body user: UserModel2): Response<TokenModel2>

    @POST("user/mobile/register")
    suspend fun userRegister(@Body user: UserModel2): Response<TokenModel2>

    @GET("wallets")
    suspend fun getWallets(@Query("email") email: String): Response<List<WalletsModel>>

    @POST("wallet")
    suspend fun saveWallet(@Body wallet: WalletsModel): Response<WalletsModel>

    @PUT("crypto/mobile")
    suspend fun updateCryptoInfo(@Body crypto: Cryptos): Response<Cryptos>

    @PUT("project/mobile")
    suspend fun updateAboutInfo(@Body project: Project) : Response<Project>

    @PUT("wallet")
    suspend fun updateWallet(@Body wallet: WalletsModel): Response<WalletsModel>

    @DELETE("wallet/mobile")
    suspend fun deleteWallet(@Query("email") userId: String, @Query("walletName") walletName: String): Response<WalletsModel>

    @POST("wallet/crypto")
    suspend fun saveCryptoToWallet(@Body cryptoToWallet: CryptoInWalletRequest): Response<WalletsModel>

    @PUT("wallet/crypto")
    suspend fun updateCryptoInWallet(@Body cryptoToWallet: CryptoInWalletRequest): Response<WalletsModel>

    @PUT("user/mobile")
    suspend fun updateUserInfo(@Body user: UserModel2): Response<UserModel2>

    @DELETE("crypto/mobile")
    suspend fun deleteCrypto(@Query("cryptoName") cryptoName: String): Response<Cryptos>

    @Multipart
    @POST("image")
    suspend fun uploadAvatar(@Part file: MultipartBody.Part): Response<ImageModel>


    @DELETE("wallet/crypto/mobile")
    suspend fun deleteCryptoInWallet(@Query("email") userId: String, @Query("walletName") walletName: String,
                                     @Query("cryptoName") cryptoName: String): Response<WalletsModel>
}


