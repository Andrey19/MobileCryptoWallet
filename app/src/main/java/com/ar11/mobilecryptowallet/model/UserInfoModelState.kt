package com.ar11.mobilecryptowallet.model

data class UserInfoModelState (
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
    val refreshingPrice: Boolean = false,
)
