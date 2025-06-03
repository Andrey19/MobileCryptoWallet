package com.ar11.mobilecryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ar11.mobilecryptowallet.auth.AppAuth2
import com.ar11.mobilecryptowallet.dto.Project
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
import kotlin.String

@ExperimentalCoroutinesApi
@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val auth2: AppAuth2,
) : ViewModel() {

    private val _projectData = SingleLiveEvent<Project>()

    val projectDataFromDb: LiveData<List<Project>> = repository.projectDataFromDb.asLiveData(
        Dispatchers.Default)


    private val defaultProject = Project(
        projectName = "Crypto Wallet",
        projectDescription = "Here you can create your crypto wallets",
        image = "",
        imageUrl = "",
        projectCost = 0.0,
    )



    init {
            loadProjectFromDb()
    }


    fun loadProject() = viewModelScope.launch {
        try {
            val projects = repository.getAllProject()
            if (!projects.isNullOrEmpty()) {
                _projectData.value = projects[0]
            }
        } catch (e: Exception) {
            _projectData.value = defaultProject
            println("------------------------- Ошибка при приеме пакета")
        }
    }

    fun loadProjectFromDb() = viewModelScope.launch {
        try {
            repository.getAllProjectFromDb()
        } catch (e: Exception) {
            println("------------------------- Ошибка при получении данных из базы данных")
        }

    }

    fun updateAboutInfo(projectName: String, projectDescription: String, imageFile: File?) = viewModelScope.launch {
        try {
            if (auth2.authStateFlow2.value.email != null) {
                var imageUrl = ""
                var image = ""
                if (imageFile != null ) {
                    val imageModel = repository.uploadImage(createMultipartBody(imageFile, "file"))
                    imageUrl = imageModel.imageUrl
                    image = imageModel.image
                }

                var projectCost = 0.0
                var sendinfo = Project(
                    projectName = projectName ,
                    projectDescription = projectDescription,
                    image = image,
                    imageUrl = imageUrl,
                    projectCost = projectCost,)
                val userInfo = repository.updateAboutInfo(sendinfo)
                _projectData.value = userInfo
            }
        } catch (e: Exception) {
            println("------------------------- Ошибка при приеме пакета с пользовательской информацией")
        }
    }


    fun createMultipartBody(uri: File, multipartName: String): MultipartBody.Part {
        val requestFile = uri.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name = multipartName, uri.name, requestFile)
    }


    fun updateProjectFromDb() {
        loadProjectFromDb()
    }


}