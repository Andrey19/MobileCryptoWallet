package com.ar11.mobilecryptowallet.auth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val email = "email"
    private val name = "name"
    private val avatarUrl = "avatarUrl"
    private val tokenKey = "token"

    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val email = prefs.getString(email, null)
        val name = prefs.getString(name, null)
        val avatarUrl = prefs.getString(avatarUrl, null)
        val token = prefs.getString(tokenKey, null)

        if (email == null || token == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(AuthState(email, name, avatarUrl, token))
        }

    }

    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(email: String, name: String, avatarUrl: String, token: String) {
        _authStateFlow.value = AuthState(email, name, avatarUrl, token)
        with(prefs.edit()) {
            putString(email, email)
            putString(name, name)
            putString(avatarUrl, avatarUrl)
            putString(tokenKey, token)
            apply()
        }

    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        with(prefs.edit()) {
            clear()
            apply()
        }
    }

}

data class AuthState(val email: String? = null, val name: String? = null,
                     val avatarUrl: String? = null, val token: String? = null)
