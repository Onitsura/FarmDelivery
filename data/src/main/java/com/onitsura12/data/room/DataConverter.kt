//package com.onitsura12.data.room
//
//import androidx.room.TypeConverter
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.onitsura12.data.models.StorageShopItem
//import java.lang.reflect.Type
//
//class DataConverter {
//
//    @TypeConverter
//    fun fromListStorageShopItem(list: List<StorageShopItem?>): String{
//        var json = ""
//        if (list.isNotEmpty()) {
//            val gson = Gson()
//            val type: Type =
//                object : TypeToken<List<StorageShopItem?>?>() {}.type
//            json = gson.toJson(list, type)
//        }
//        return json
//    }
//
//    @TypeConverter
//    fun toListStorageShopItem(json: String): List<StorageShopItem>{
//        val list = ArrayList<StorageShopItem>()
//        if (json.isNotBlank() && json.isNotEmpty()) {
//            val gson = Gson()
//            val type =
//                object : TypeToken<List<StorageShopItem>?>() {}.type
//             gson.fromJson(json, type)
//        }
//    }
//}