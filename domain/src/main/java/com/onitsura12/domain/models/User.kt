package com.onitsura12.domain.models

data class User(
    var name: String? = "",
    val lastName: String? = "",
    var phone: String = "",
    var eMail: String? = "",
    var photoUrl: String? = "",
    val orders: List<Order?> = listOf(),
    val address: Address? = null,
    val cart: List<ShopItem?> = listOf()
)
