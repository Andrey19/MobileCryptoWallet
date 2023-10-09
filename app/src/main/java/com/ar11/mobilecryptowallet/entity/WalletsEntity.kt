package com.ar11.mobilecryptowallet.entity

import androidx.room.Entity

import com.ar11.mobilecryptowallet.dto.CryptosModel
import com.ar11.mobilecryptowallet.dto.WalletsModel


@Entity(primaryKeys = ["userId", "walletName"])
data class WalletsEntity (
    val userId: String,
    val walletName: String,
    val walletDescription: String,
    val cryptosCount: Double,
    val cryptosCost: Double,
    val cryptocurrenciesList: MutableList<CryptosModel>?
)
{
    fun toDto() = WalletsModel(userId, walletName, walletDescription, cryptosCount, cryptosCost,cryptocurrenciesList)

    companion object {
        fun fromDto(dto: WalletsModel) =
            WalletsEntity(dto.userId, dto.walletName, dto.walletDescription, dto.cryptosCount,
                dto.cryptosCost, dto.cryptocurrenciesList)

    }
}

fun List<WalletsEntity>.toDto(): List<WalletsModel> = map( WalletsEntity::toDto)
fun List<WalletsModel>.toEntity(): List<WalletsEntity> = map { WalletsEntity.fromDto(it) }
