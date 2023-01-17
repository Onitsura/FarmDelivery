package com.onitsura12.farmdel.fragments.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.domain.models.Address
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.utils.FirebaseHelper

class ShopViewModel : ViewModel() {

    private val _shopItemList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val shopItemList: LiveData<ArrayList<ShopItem>> = _shopItemList



    init {
        initSuppliesList()
    }

    private fun initSuppliesList(){
        val list = arrayListOf<ShopItem>()
        FirebaseHelper.REF_DATABASE_ROOT.child(FirebaseHelper.NODE_SUPPLIES)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(supplySnapshot in snapshot.children){
                        val address = supplySnapshot.getValue(ShopItem::class.java)
                        list.add(address!!)
                        _shopItemList.value = list
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }














}