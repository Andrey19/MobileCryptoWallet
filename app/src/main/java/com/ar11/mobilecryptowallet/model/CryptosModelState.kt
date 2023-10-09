package com.ar11.mobilecryptowallet.model

data class CryptosModelState (
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
)
