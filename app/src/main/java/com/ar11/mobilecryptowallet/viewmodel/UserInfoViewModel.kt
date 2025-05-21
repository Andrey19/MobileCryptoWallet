package com.ar11.mobilecryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ar11.mobilecryptowallet.auth.AppAuth2
import com.ar11.mobilecryptowallet.model.UserModel2
import com.ar11.mobilecryptowallet.repository.CryptoRepository
import com.ar11.mobilecryptowallet.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class UserInfoViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val auth2: AppAuth2,
) : ViewModel() {

    private val _userData = SingleLiveEvent<UserModel2>()

    val userData: LiveData<UserModel2>
        get() = _userData

    init {
        loadUserInfo()
    }

    fun loadUserInfo() = viewModelScope.launch {
        try {
            if (auth2.authStateFlow2.value.email != null) {
                val userInfo = repository.getUser(auth2.authStateFlow2.value.email!!)
                _userData.value = userInfo
            }
        } catch (e: Exception) {
            println("------------------------- Ошибка при приеме пакета с пользовательской информацией")
        }
    }

    fun updateUserInfo(userName: String, imageFile: File?) = viewModelScope.launch {
        try {
            if (auth2.authStateFlow2.value.email != null) {
                var imageUrl = ""
                var image = ""
                if (imageFile != null ) {
                    val imageModel = repository.uploadImage(createMultipartBody(imageFile, "file"))
                    imageUrl = imageModel.imageUrl
                    image = imageModel.image
                }


                val userInfoForUpdate = UserModel2(
                    email = auth2.authStateFlow2.value.email!!,
                    name = userName,
                    avatarUrl = imageUrl,
                    avatar = image
                )

                val userInfo = repository.updateUserInfo(userInfoForUpdate)
                _userData.value = userInfo
            }
        } catch (e: Exception) {
            println("------------------------- Ошибка при приеме пакета с пользовательской информацией")
        }
    }

    fun createMultipartBody(uri: File, multipartName: String): MultipartBody.Part {
        val requestFile = uri.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name = multipartName, uri.name, requestFile)
    }


    fun updateUserData() {
        loadUserInfo()
    }



}