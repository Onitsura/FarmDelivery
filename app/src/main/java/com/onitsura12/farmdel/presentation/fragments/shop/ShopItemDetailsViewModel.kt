package com.onitsura12.farmdel.presentation.fragments.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.farmdel.domain.models.ShopItem
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.farmdel.utils.REF_DATABASE_ROOT
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShopItemDetailsViewModel @Inject constructor() : ViewModel() {
    private val _imagesList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val imagesList: LiveData<ArrayList<String>> = _imagesList
    private val _shopItem: MutableLiveData<ShopItem?> = MutableLiveData()
    val shopItem: LiveData<ShopItem?> = _shopItem


    init {
        _imagesList.value = arrayListOf()

    }


    fun getImages(title: String){
        val list: ArrayList<String> = arrayListOf()

        REF_DATABASE_ROOT.child(NODE_SUPPLIES)
           .addValueEventListener(object: ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for(shopItemSnapshot in snapshot.children){
                        val shopItem = shopItemSnapshot.getValue(ShopItem::class.java)
                        if (shopItem != null && shopItem.title == title){
                            _shopItem.value = shopItem
                            for (i in shopItem.imagesArray?.indices!!){
                                list.add(shopItem.imagesArray[i])
                            }
                        }


                    }
                    _imagesList.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }




}