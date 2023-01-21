package com.onitsura12.data.models


import java.util.*

data class StorageShopItem(
    val title: String = "",
    val cost: String = "",
    val count: String? = "",
    val weight: String? = "",
    val imagePath: String? = "",
    val deliveryDate: Date? = null
)