package com.onitsura12.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity
data class StorageShopItemModel(
//    @PrimaryKey
    val title: String,
    val cost: String,
    val count: String?,
    val weight: String?,
    val imagePath: String?
)
