package com.ar11.mobilecryptowallet.dto

data class CryptoInWalletRequest(
    val userId: String,
    val walletName: String,
    val cryptoName: String = "",
    val cryptoType: String = "",
    val image: String = "",
    val imageUrl: String = "",
    val cryptoDescription: String = "",
    val cryptoAmount: Double = 0.0,
    val cryptoCost: Double = 0.0,
)
