package com.onitsura12.farmdel.fragments.root

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.AUTH
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_SUPPLY_COST
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_SUPPLY_COUNT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_SUPPLY_DELIVERY_DATE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_SUPPLY_IMAGE_PATH
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_SUPPLY_TITLE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_SUPPLY_WEIGHT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_WHITELIST
import com.onitsura12.data.storage.firebase.utils.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.UID
import com.onitsura12.domain.models.ShopItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangeDeliveryDateViewModel @Inject constructor() : ViewModel() {

    private val _root: MutableLiveData<Boolean> = MutableLiveData()
    val root: LiveData<Boolean> = _root
    private val _shopItemList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val shopItemList: LiveData<ArrayList<ShopItem>> = _shopItemList
    private val _adapterList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val adapterList: LiveData<ArrayList<ShopItem>> = _adapterList


    init {
        UID = AUTH.currentUser!!.uid
        isInWhiteList(UID)
        initSuppliesList()


    }


    private fun isInWhiteList(id: String) {
        _root.value = false
        REF_DATABASE_ROOT.child(NODE_WHITELIST)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (supplySnapshot in snapshot.children) {
                        val whiteListItem = supplySnapshot.getValue(String::class.java)
                        if (id == whiteListItem) {
                            _root.value = true
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }


    fun changeDeliveryDate(shopItem: ShopItem) {
        val dataMap = mutableMapOf<String, Any?>()
        dataMap[CHILD_SUPPLY_IMAGE_PATH] = shopItem.imagePath
        dataMap[CHILD_SUPPLY_COUNT] = shopItem.count
        dataMap[CHILD_SUPPLY_TITLE] = shopItem.title
        dataMap[CHILD_SUPPLY_COST] = shopItem.cost
        dataMap[CHILD_SUPPLY_WEIGHT] = shopItem.weight
        dataMap[CHILD_SUPPLY_DELIVERY_DATE] = shopItem.deliveryDate


        REF_DATABASE_ROOT.child(NODE_SUPPLIES)
            .child(shopItem.title)
            .updateChildren(dataMap)
            .addOnCompleteListener {

            }

    }

    private fun initSuppliesList() {
        val list = arrayListOf<ShopItem>()
        REF_DATABASE_ROOT.child(NODE_SUPPLIES)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (supplySnapshot in snapshot.children) {
                        val shopItem = supplySnapshot.getValue(ShopItem::class.java)
                        list.add(shopItem!!)

                    }

                    _shopItemList.value = list
                    _adapterList.value = list
                }


                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun removeShopItem(shopItem: ShopItem) {
        REF_DATABASE_ROOT.child(NODE_SUPPLIES)
            .child(shopItem.title)
            .removeValue()
    }




}