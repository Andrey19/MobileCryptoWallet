package com.ar11.mobilecryptowallet.util

import androidx.room.TypeConverter
import com.ar11.mobilecryptowallet.dto.CryptosModel
import com.google.gson.Gson

class CryptoConverter {
    @TypeConverter
    fun listToJsonString(value: List<CryptosModel>?): String {
        if(value.isNullOrEmpty()) return ""
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonStringToList(value: String): List<CryptosModel> {

        return try {
            val list = Gson().fromJson(value, Array<CryptosModel>::class.java).toList()
            list
        } catch (e: Exception) {
            emptyList()
        }

    }
}
