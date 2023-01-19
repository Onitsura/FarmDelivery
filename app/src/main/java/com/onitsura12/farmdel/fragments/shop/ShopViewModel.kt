package com.onitsura12.farmdel.fragments.shop

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.utils.FirebaseHelper
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_COST
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_COUNT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_DELIVERY_DATE
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_IMAGE_PATH
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_TITLE
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_WEIGHT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_WHITELIST
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.UID
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.USER

class ShopViewModel : ViewModel() {

    private val _shopItemList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val shopItemList: LiveData<ArrayList<ShopItem>> = _shopItemList
    private val _showAddButton: MutableLiveData<Boolean> = MutableLiveData()
    val showAddButton: LiveData<Boolean> = _showAddButton
    private val isCartEmpty: MutableLiveData<Boolean> = MutableLiveData()
    private val cartList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()


    init {
        initSuppliesList()
        getCart()
        isCartEmpty()

    }

    private fun initSuppliesList() {
        val list = arrayListOf<ShopItem>()
        REF_DATABASE_ROOT.child(NODE_SUPPLIES)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (supplySnapshot in snapshot.children) {
                        val shopItem = supplySnapshot.getValue(ShopItem::class.java)
                        list.add(shopItem!!)
                        _shopItemList.value = list
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }



    private fun getCart(){


    }

     private fun isCartEmpty(){
         REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CART).get().addOnCompleteListener {
             if (it.result.value.toString() == "null"){
                 isCartEmpty.value = true
             }
         }
    }

     fun addNewCartItem(cartItem: ShopItem) {
        cartList.value?.add(cartItem)

//        REF_DATABASE_ROOT.child(NODE_USERS)
//            .child(UID)
//            .child(CHILD_CART)
//            .child(cartItem.title)
//            .child(CHILD_CART_COUNT)
//            .setValue(cartItem.count)

         val dataMap = mutableMapOf<String, Any?>()
         dataMap[CHILD_CART_IMAGE_PATH] =
         dataMap[CHILD_CART_COUNT] = cartItem.count
         dataMap[CHILD_CART_TITLE] = cartItem.title
         dataMap[CHILD_CART_COST] = cartItem.cost
         dataMap[CHILD_CART_WEIGHT] = cartItem.weight
         dataMap[CHILD_CART_DELIVERY_DATE] = cartItem.deliveryDate


         REF_DATABASE_ROOT.child(NODE_USERS)
             .child(UID)
             .child(CHILD_CART)
             .child(cartItem.title)
             .updateChildren(dataMap)
             .addOnCompleteListener {
                 if (it.isComplete) {
                     Log.i("cart", "cart added")

                 }
             }



    }




    fun isInWhiteList(id: String) {
        _showAddButton.value = false
        REF_DATABASE_ROOT.child(NODE_WHITELIST)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (supplySnapshot in snapshot.children) {
                        val whiteListItem = supplySnapshot.getValue(String::class.java)
                        if (id == whiteListItem) {
                            _showAddButton.value = true
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }


}