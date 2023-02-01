package com.onitsura12.farmdel.domain.models


data class Address(
    val city: String? = "",
    val street: String? = "",
    val house: String? = "",
    val entrance: String? = "",
    val floor: String? = "",
    val flat: String? = "",
    val id: String = "",
    var primary: Boolean = false
)
