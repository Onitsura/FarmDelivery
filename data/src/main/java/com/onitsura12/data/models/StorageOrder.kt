package com.onitsura12.data.models

data class StorageOrder (
    val number: String,
    val items: List<StorageOrderItem>,
    val amount: Int
    )

    data class StorageOrderItem(
        val title: String,
        val count: Int,
        val price: Int
    )