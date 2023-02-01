package com.onitsura12.farmdel.domain.models

data class User(
    var fullname: String? = "",
    val lastName: String? = "",
    var phone: String = "",
    var eMail: String? = "",
    var photoUrl: String? = "",
    var orders: List<Order?> = listOf(),
    var address: Address? = null,
    var cart: List<ShopItem?> = listOf(),
    var token: String? = ""
)
