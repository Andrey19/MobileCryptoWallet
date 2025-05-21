package com.ar11.mobilecryptowallet.util

import androidx.room.TypeConverter
import com.ar11.mobilecryptowallet.dto.CryptosModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CryptoConverter {
    @TypeConverter
    fun listToJsonString(value: MutableList<CryptosModel>?): String {
        if(value.isNullOrEmpty()) return ""
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonStringToList(value: String): MutableList<CryptosModel> {
        try {
            val cryptosModelListType = object : TypeToken<MutableList<CryptosModel>?>() {}.type

            return Gson().fromJson(value, cryptosModelListType)
        } catch (e: Exception) {
            return mutableListOf()
        }
    }
}

