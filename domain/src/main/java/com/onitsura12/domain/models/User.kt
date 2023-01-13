package com.onitsura12.domain.models

data class User(
    val name: String?,
    val lastName: String?,
    val phone: String,
    val eMail: String?,
    val orders: List<Order?>,
    val address: Address?,
    val cart: List<ShopItem?>
)
