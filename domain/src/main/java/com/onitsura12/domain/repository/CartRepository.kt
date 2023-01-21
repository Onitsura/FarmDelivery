package com.onitsura12.domain.repository

import com.onitsura12.domain.models.ShopItem

interface CartRepository {

    fun getCart(): ArrayList<ShopItem>

    fun saveCart(cart: ArrayList<ShopItem>)

}