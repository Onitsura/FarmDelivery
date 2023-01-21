package com.onitsura12.farmdel.fragments.cart


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.USER
import com.onitsura12.domain.models.ShopItem


class CartViewModel : ViewModel() {
    private val _cart: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val cart: LiveData<ArrayList<ShopItem>> = _cart




    init {
        initCart()
        checkTitles()

    }





    private fun initCart(){
        val list = arrayListOf<ShopItem>()
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_CART)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children){
                        val cartItem = cartSnapshot.getValue(ShopItem::class.java)
                        list.add(cartItem!!)

                    }
                    _cart.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }

    private fun checkTitles(){
        val list = arrayListOf<ShopItem>()
        REF_DATABASE_ROOT
            .child(NODE_SUPPLIES)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children){
                        val cartItem = cartSnapshot.getValue(ShopItem::class.java)
                        list.add(cartItem!!)

                    }
                    val removeList = arrayListOf<ShopItem>()
                    if (list.size != _cart.value!!.size){

                        for (i in _cart.value!!.indices) {
                            var match = false
                            for (j in list.indices) {
                                if (_cart.value!![i].title == list[j].title){
                                    match = true
                                }
                                Log.i("match", list[j].title)
                            }
                            Log.i("match", match.toString())

                            if (!match){
                                removeList.add(_cart.value!![i])
                            }
                        }
                    }
                    _cart.value!!.removeAll(removeList.toSet())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }




    private fun addNewCartItem(cartItem: ShopItem) {

        val dataMap = mutableMapOf<String, Any?>()
        dataMap[FirebaseHelper.CHILD_CART_IMAGE_PATH] = cartItem.imagePath
        dataMap[FirebaseHelper.CHILD_CART_COUNT] = cartItem.count
        dataMap[FirebaseHelper.CHILD_CART_TITLE] = cartItem.title
        dataMap[FirebaseHelper.CHILD_CART_COST] = cartItem.cost
        dataMap[FirebaseHelper.CHILD_CART_WEIGHT] = cartItem.weight
        dataMap[FirebaseHelper.CHILD_CART_DELIVERY_DATE] = cartItem.deliveryDate


        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_CART)
            .child(cartItem.title)
            .updateChildren(dataMap)
            .addOnCompleteListener {

            }


    }

    fun incrementItemCount(cartItem: ShopItem) {
        val newValue = cartItem.count?.toInt()?.plus(1)
        cartItem.count = newValue.toString()
        addNewCartItem(cartItem = cartItem)
    }

    fun decrementItemCount(cartItem: ShopItem) {
        if (cartItem.count?.toInt()!! > 0) {
            val newValue = cartItem.count?.toInt()?.minus(1)
            cartItem.count = newValue.toString()
            addNewCartItem(cartItem = cartItem)
        }
    }


}


