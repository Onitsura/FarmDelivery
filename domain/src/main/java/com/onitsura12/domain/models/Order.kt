package com.onitsura12.domain.models

import java.util.Date

data class Order(
    val id: String = "",
    val number: String = "0",
    val items: ArrayList<ShopItem> = arrayListOf(),
    val address: Address? = null,
    val userPhone: String? = null,
    val userName: String? = null,
    val amount: Int? = 0,
    val placed: Date? = null,
    val token: String? = ""
)

data class OrderItem(
    val title: String,
    val count: Int,
    val price: Int,
    val deliveryDate: Date?,
)

data class AdditionalServices(
    val title: String = "",
    val price: String = "0",
    var isAdded: Boolean = false
)
