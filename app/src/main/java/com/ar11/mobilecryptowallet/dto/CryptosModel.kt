package com.ar11.mobilecryptowallet.dto

data class CryptosModel (
    val cryptoName: String,
    val cryptoType: String,
    val image: String,
    val imageUrl: String,
    val cryptoDescription: String,
    val cryptoAmount: Double,
    val cryptoCost: Double,
)
