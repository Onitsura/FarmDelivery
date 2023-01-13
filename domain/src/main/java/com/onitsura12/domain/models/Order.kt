package com.onitsura12.domain.models

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
