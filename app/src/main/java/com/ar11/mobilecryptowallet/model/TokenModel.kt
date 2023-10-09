package com.ar11.mobilecryptowallet.model

data class TokenModel(
    val email: String = "",
    val name: String = "",
    val avatar: String = "",
    val avatarUrl: String = "",
    val password: String = "",
    val isAdmin: Boolean = false,
    val role: String = "",
    val token: String = ""
)

