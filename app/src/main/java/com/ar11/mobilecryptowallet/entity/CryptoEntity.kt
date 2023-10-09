package com.ar11.mobilecryptowallet.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ar11.mobilecryptowallet.dto.Cryptos


@Entity
data class CryptoEntity(
    @PrimaryKey
    val cryptoName: String,
    val image: String,
    val imageUrl: String,
    val cryptoDescription: String,
    val cryptoAmount: Double,
    val cryptoCost: Double,

) {
    fun toDto() = Cryptos(cryptoName,image,imageUrl,cryptoDescription,cryptoAmount,cryptoCost)

    companion object {
        fun fromDto(dto: Cryptos) =
            CryptoEntity(dto.cryptoName, dto.image, dto.imageUrl,
                dto.cryptoDescription, dto.cryptoAmount, dto.cryptoCost)
    }
}

fun List<CryptoEntity>.toDto(): List<Cryptos> = map( CryptoEntity::toDto)
fun List<Cryptos>.toEntity(): List<CryptoEntity> = map { CryptoEntity.fromDto(it) }
