package com.ar11.mobilecryptowallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ar11.mobilecryptowallet.dto.Project
import com.ar11.mobilecryptowallet.repository.CryptoRepository
import com.ar11.mobilecryptowallet.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val repository: CryptoRepository,
) : ViewModel() {

    private val _projectData = SingleLiveEvent<Project>()

    val projectDataFromDb: LiveData<List<Project>> = repository.projectDataFromDb.asLiveData(
        Dispatchers.Default)

    val projectData: LiveData<Project>
        get() = _projectData

    private val defaultProject = Project(
        projectName = "Crypto Wallet",
        projectDescription = "Here you can create your crypto wallets",
        image = "",
        imageUrl = "",
        projectCost = 0.0,
    )

    val isFromDb = true


    init {
        if (isFromDb) {
            loadProjectFromDb()
        } else {
            loadProject()
        }
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

    fun updateProjectFromDb() {
        loadProjectFromDb()
    }


    fun updateProject() {
        loadProject()
    }


}