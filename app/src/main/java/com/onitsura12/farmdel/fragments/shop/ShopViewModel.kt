package com.onitsura12.farmdel.fragments.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onitsura12.domain.models.ShopItem

class ShopViewModel : ViewModel() {

     val shopItemList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
//    val shopItemList: LiveData<ArrayList<ShopItem>> = _shopItemList
//    val newItem: MutableLiveData<ShopItem> = MutableLiveData()

    //TODO пока заглушаем правильную логику, и напрямую кидаем новые элеменеты в список, дальше
    // будем тянуть из Firebase
    init {
        shopItemList.value = arrayListOf()
    }

    fun convertToShopItem(arrayList: ArrayList<String>): ShopItem{
        return ShopItem(
            title = arrayList[0],
            cost = arrayList[1],
            imagePath = arrayList[2],
            count = arrayList[3],
            weight = arrayList[4]
        )
    }














}