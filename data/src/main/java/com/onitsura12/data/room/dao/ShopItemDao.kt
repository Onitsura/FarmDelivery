//package com.onitsura12.data.room.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//import com.onitsura12.data.models.StorageShopItemModel
//
//@Dao
//interface ShopItemDao {
//    @Query("SELECT * FROM storageshopitemmodel WHERE title = :title")
//    fun getByTitle(title: String): StorageShopItemModel
//
//    @Insert
//    fun insert(storageShopItemModel: StorageShopItemModel)
//}