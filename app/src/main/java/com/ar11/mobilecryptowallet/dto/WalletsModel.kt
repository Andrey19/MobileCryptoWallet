package com.ar11.mobilecryptowallet.dto

data class WalletsModel (
    val userId: String = "",
    val walletName: String = "",
    val walletDescription: String = "",
    val cryptosCount: Double = 0.0,
    val cryptosCost: Double = 0.0,
    val cryptocurrenciesList: MutableList<CryptosModel>? = null
)
