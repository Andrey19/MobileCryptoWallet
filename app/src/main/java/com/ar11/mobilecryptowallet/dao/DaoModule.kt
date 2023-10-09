package com.ar11.mobilecryptowallet.dao

import com.ar11.mobilecryptowallet.db.AppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
    @Provides
    fun provideCryptoDao(db: AppDb): CryptoDao = db.cryptoDao()
    @Provides
    fun provideWalletsDao(db: AppDb): WalletsDao = db.walletsDao()
}
