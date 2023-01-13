package com.onitsura12.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onitsura12.domain.models.Address
import com.onitsura12.domain.models.Order
import com.onitsura12.domain.models.ShopItem


//@Entity
data class StorageUserModel(
//    @PrimaryKey
    val phone: String,
    val name: String?,
    val lastName: String?,
    val eMail: String?,
//    @Embedded
    val orders: List<Order?>,
//    @Embedded
    val address: Address?,
//    @Embedded
    val cart: List<ShopItem?>
)
