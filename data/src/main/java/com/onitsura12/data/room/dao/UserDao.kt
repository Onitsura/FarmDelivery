//package com.onitsura12.data.room.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//import com.onitsura12.data.models.StorageUserModel
//
//@Dao
//interface UserDao {
//    @Query("SELECT * FROM storageusermodel WHERE phone = :phone")
//    fun getByPhone(phone: String): StorageUserModel
//
//    @Insert
//    fun insert(storageUserModel: StorageUserModel)
//
//}