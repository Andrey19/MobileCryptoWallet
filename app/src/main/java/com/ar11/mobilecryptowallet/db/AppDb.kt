package com.ar11.mobilecryptowallet.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ar11.mobilecryptowallet.dao.CryptoDao
import com.ar11.mobilecryptowallet.dao.WalletsDao
import com.ar11.mobilecryptowallet.entity.CryptoEntity
import com.ar11.mobilecryptowallet.entity.WalletsEntity
import com.ar11.mobilecryptowallet.util.CryptoConverter


@Database(entities = [CryptoEntity::class, WalletsEntity::class], version = 2, exportSchema = false)
@TypeConverters(CryptoConverter::class)
abstract class AppDb : RoomDatabase() {

    abstract fun cryptoDao(): CryptoDao
    abstract fun walletsDao(): WalletsDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
