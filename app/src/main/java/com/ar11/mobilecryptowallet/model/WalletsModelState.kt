package com.ar11.mobilecryptowallet.model

data class WalletsModelState (
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
    val updating: Boolean = false,
    val adding: Boolean = false,
    val deleting: Boolean = false,
    val updatingCrypto: Boolean = false,
    val addingCrypto: Boolean = false,
    val deletingCrypto: Boolean = false,
)



