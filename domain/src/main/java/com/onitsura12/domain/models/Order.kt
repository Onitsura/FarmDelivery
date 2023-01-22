package com.onitsura12.domain.models

import java.util.Date

data class Order(
    val id: String = "",
    val number: String = "",
    val items: ArrayList<ShopItem> = arrayListOf(),
    val address: Address? = null,
    val userPhone: String? = null,
    val userName: String? = null,
    val amount: Int? = 0
)

data class OrderItem(
    val title: String,
    val count: Int,
    val price: Int,
    val deliveryDate: Date?
)
