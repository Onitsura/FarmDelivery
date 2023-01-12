package com.onitsura12.farmdel.models

data class Order(
    val number: String,
    val items: List<OrderItem>,
    val amount: Int
)

data class OrderItem(
    val title: String,
    val count: Int,
    val price: Int
)
