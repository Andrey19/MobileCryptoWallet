package com.ar11.mobilecryptowallet.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import com.ar11.mobilecryptowallet.entity.CryptoEntity


@Dao
interface CryptoDao {

    @Query("SELECT * FROM CryptoEntity ")
    fun getAll(): Flow<List<CryptoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crypto: CryptoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cryptos: List<CryptoEntity>)

}

