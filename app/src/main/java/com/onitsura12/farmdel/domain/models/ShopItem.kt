package com.onitsura12.farmdel.domain.models

data class ShopItem(
    var title: String = "",
    var cost: String = "0",
    val per: String? = "",
    var count: String? = "0",
    var weight: String? = "0",
    var imagePath: String? = "",
    var deliveryDate: String? = null,
    val description: String? = "",
    val imagesArray: ArrayList<String>? = arrayListOf(),
    val additionalServices: AdditionalServices? = null
)
