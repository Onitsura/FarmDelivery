package com.onitsura12.farmdel.fragments.shop

import android.content.ContentResolver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onitsura12.domain.models.Address
import com.onitsura12.domain.models.ShopItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class AddShopItemViewModel() : ViewModel() {

    val shopItem: MutableLiveData<ShopItem> = MutableLiveData()
    val imagePath: MutableLiveData<String> = MutableLiveData()

    fun saveNewItem(): Flow<ShopItem> {
        return shopItem.value!!.asFlow()
    }

    fun convertToArrayList(item: ShopItem): ArrayList<String?> {
        return arrayListOf(
            item.title,
            item.cost,
            item.imagePath,
            item.count,
            item.weight
        )

    }


}


private fun ShopItem.asFlow(): Flow<ShopItem> {
    val list = mutableListOf(this)
    return flow {
        emitAll(list.asFlow())
    }.flowOn(Dispatchers.IO)
}


