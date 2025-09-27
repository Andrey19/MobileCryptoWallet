package com.ar11.mobilecryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.avatar
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.avatarUrl
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.isAdmin
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.name
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.password
import com.ar11.mobilecryptowallet.activity.UserDetailFragment.Companion.role
import com.ar11.mobilecryptowallet.auth.AppAuth2
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.model.UserModel2
import com.ar11.mobilecryptowallet.repository.CryptoRepository
import com.ar11.mobilecryptowallet.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val auth2: AppAuth2,
) : ViewModel() {

    private val _userData = SingleLiveEvent<UserModel2>()

    val authState: LiveData<AppAuth2.AuthState2> =
        auth2.authStateFlow2.asLiveData(Dispatchers.Default)


    val userData: LiveData<UserModel2>
        get() = _userData

    fun updateUserName(user: UserModel2, imageFile: File?) = viewModelScope.launch {
        try {
            if (auth2.authStateFlow2.value.email != null) {
                var sendUser = user
                if (imageFile != null) {
                    val imageModel = repository.uploadImage(createMultipartBody(imageFile, "file"))
                    sendUser = UserModel2(
                        email = user.email,
                        name = user.name,
                        avatar = imageModel.image,
                        avatarUrl = imageModel.imageUrl,

                        )
                }

                val userInfo = repository.updateUserInfo2(sendUser)
                _userData.value = userInfo
                println("------------------------- Криптовалюта сохранена успешно")
            }
        } catch (e: Exception) {
            println("------------------------- Ошибка при сохранении криптовалюты")
        }
    }

    fun saveCryptoInfo(crypto: Cryptos, imageFile: File?) = viewModelScope.launch {
        try {
            if (auth2.authStateFlow2.value.email != null) {
                var sendCrypto = crypto
                if (imageFile != null) {
                    val imageModel = repository.uploadImage(createMultipartBody(imageFile, "file"))
                    sendCrypto = Cryptos(
                        cryptoName = crypto.cryptoName,
                        image = imageModel.image,
                        imageUrl = imageModel.imageUrl,
                        cryptoDescription = crypto.cryptoDescription,
                        cryptoAmount = crypto.cryptoAmount,
                        cryptoCost = crypto.cryptoCost
                    )
                }

                val cryptoInfo = repository.saveCryptoInfo(sendCrypto)
                //  _cryptoData.value = cryptoInfo
                println("------------------------- Криптовалюта сохранена успешно")

            }
        } catch (e: Exception) {
            println("------------------------- Ошибка при сохранении криптовалюты")
        }
    }

    fun deleteCrypto(cryptoName: String) = viewModelScope.launch {
        try {
            val cryptoInfo = repository.deleteCrypto(cryptoName)
            println("------------------------- Криптовалюта удалена успешно")
        } catch (e: Exception) {
            println("------------------------- Ошибка при удалении криптовалюты")
        }
    }


    fun createMultipartBody(uri: File, multipartName: String): MultipartBody.Part {
        val requestFile = uri.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name = multipartName, uri.name, requestFile)
    }


}
