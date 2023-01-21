package com.onitsura12.data.models

import com.onitsura12.domain.models.Address
import com.onitsura12.domain.models.Order
import com.onitsura12.domain.models.ShopItem


data class StorageUserModel(
    val name: String? = "",
    val lastName: String? = "",
    val phone: String = "",
    val eMail: String? = "",
    val photoUrl: String? = "",
    val orders: List<Order?> = listOf(),
    val address: Address? = null,
    val cart: List<ShopItem?> = listOf()
)
