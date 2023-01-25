package com.onitsura12.domain.models

import java.util.Date

data class ShopItem(
    var title: String = "",
    var cost: String = "",
    val per: String? = "",
    var count: String? = "0",
    var weight: String? = "",
    var imagePath: String? = "",
    var deliveryDate: String? = null
)
