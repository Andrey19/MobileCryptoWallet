package com.ar11.mobilecryptowallet.dao

import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ar11.mobilecryptowallet.entity.WalletsEntity


@Dao
interface WalletsDao {

    @Query("SELECT * FROM WalletsEntity")
    fun getAll(): Flow<List<WalletsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallet: WalletsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallets: List<WalletsEntity>)

    @Query("DELETE FROM WalletsEntity WHERE userId = :userId AND walletName = :walletName")
    suspend fun deleteWallet(userId: String, walletName: String)

}

