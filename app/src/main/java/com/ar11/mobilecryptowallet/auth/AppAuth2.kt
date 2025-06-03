package com.ar11.mobilecryptowallet.auth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth2 @Inject constructor(
    @ApplicationContext private val context: Context,
){
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val email = "email"
    private val name = "name"
    private val avatarUrl = "avatarUrl"
    private val tokenKey = "token"
    private val isAdminVal = "isAdmin"

    private val _authStateFlow2: MutableStateFlow<AuthState2>

    data class AuthState2(val email: String? = null, val name: String? = null,
                          val avatarUrl: String? = null, val token: String? = null,
                          val isAdmin: Boolean? = null)


    init {
        val email = prefs.getString(email, null)
        val name = prefs.getString(name, null)
        val avatarUrl = prefs.getString(avatarUrl, null)
        val token = prefs.getString(tokenKey, null)
        val isAdmin = prefs.getBoolean(isAdminVal, false)

        if (email == null || token == null) {
            _authStateFlow2 = MutableStateFlow(AuthState2())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow2 = MutableStateFlow(AuthState2(email, name, avatarUrl, token, isAdmin))
        }

    }

    val authStateFlow2: StateFlow<AuthState2> = _authStateFlow2.asStateFlow()

    @Synchronized
    fun setAuth2(email: String, name: String, avatarUrl: String, token: String, isAdmin: Boolean) {
        _authStateFlow2.value = AuthState2(email, name, avatarUrl, token, isAdmin)
        with(prefs.edit()) {
            putString(email, email)
            putString(name, name)
            putString(avatarUrl, avatarUrl)
            putString(tokenKey, token)
            putBoolean(isAdminVal, isAdmin)
            apply()
        }

    }

    @Synchronized
    fun removeAuth2() {
        _authStateFlow2.value = AuthState2()
        with(prefs.edit()) {
            clear()
            apply()
        }
    }
}



