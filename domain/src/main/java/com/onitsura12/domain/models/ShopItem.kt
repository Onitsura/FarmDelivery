package com.onitsura12.domain.models

import java.util.Date

data class ShopItem(
    val title: String = "",
    val cost: String = "",
    var count: String? = "0",
    val weight: String? = "",
    var imagePath: String? = "",
    var deliveryDate: String? = null
)
