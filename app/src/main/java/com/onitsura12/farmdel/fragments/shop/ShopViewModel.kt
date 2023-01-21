package com.onitsura12.farmdel.fragments.shop


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_COST
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_COUNT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_DELIVERY_DATE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_IMAGE_PATH
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_TITLE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_WEIGHT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_WHITELIST
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.USER
import com.onitsura12.domain.models.ShopItem


class ShopViewModel : ViewModel() {

    private val _shopItemList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val shopItemList: LiveData<ArrayList<ShopItem>> = _shopItemList
    private val _showAddButton: MutableLiveData<Boolean> = MutableLiveData()
    val showAddButton: LiveData<Boolean> = _showAddButton
    private val isCartEmpty: MutableLiveData<Boolean> = MutableLiveData()
    private val _cart: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val cart: LiveData<ArrayList<ShopItem>> = _cart
    private val _adapterList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val adapterList: LiveData<ArrayList<ShopItem>> = _adapterList

    init {

        initSuppliesList()
        getCart()


    }


    private fun initSuppliesList() {
        val list = arrayListOf<ShopItem>()
        REF_DATABASE_ROOT.child(NODE_SUPPLIES)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (supplySnapshot in snapshot.children) {
                        val shopItem = supplySnapshot.getValue(ShopItem::class.java)
                        list.add(shopItem!!)
                    }
                    _shopItemList.value = list
                    _adapterList.value = list
                    Log.i("async", "shop")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    private fun getCart() {
        val list = arrayListOf<ShopItem>()
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_CART)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnapshot in snapshot.children) {
                        val cartItem = cartSnapshot.getValue(ShopItem::class.java)

                        list.add(cartItem!!)


                    }
                    _cart.value = list
                    USER.cart = list
                    Log.i("async", "cart")
                    updateCart()


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }


    private fun updateCart() {

        val list = arrayListOf<ShopItem>()
        REF_DATABASE_ROOT.child(NODE_SUPPLIES)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (supplySnapshot in snapshot.children) {
                        val shopItem = supplySnapshot.getValue(ShopItem::class.java)
                        list.add(shopItem!!)
                    }
                    for (i in _cart.value!!.indices) {
                        if (list.contains(_cart.value!![i])) {
                            list.remove(_cart.value!![i])
                        }
                    }
                    for (i in list.indices) {
                        addNewCartItem(list[i])
                    }
                    _cart.value!!.addAll(list)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun updateAdapterList(){
        val list = arrayListOf<ShopItem>()
        REF_DATABASE_ROOT.child(NODE_SUPPLIES)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (supplySnapshot in snapshot.children) {
                        val shopItem = supplySnapshot.getValue(ShopItem::class.java)
                        list.add(shopItem!!)
                    }
                    for (i in _cart.value!!.indices) {
                        if (list.contains(_cart.value!![i])) {
                            list.remove(_cart.value!![i])
                        }
                    }
                    for (i in list.indices) {
                        addNewCartItem(list[i])
                    }
                    _cart.value!!.addAll(list)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


    //Метод сверки тайтлов в корзине и сапплаях
    private fun checkTitles(cartList: ArrayList<ShopItem>, shopList: ArrayList<ShopItem>) {
        for (i in 0 until shopList.size) {
            if (!cartList.contains(element = shopList[i])) {
                cartList.add(shopList[i])
            }
        }
    }


    private fun addNewCartItem(cartItem: ShopItem) {

        val dataMap = mutableMapOf<String, Any?>()
        dataMap[CHILD_CART_IMAGE_PATH] = cartItem.imagePath
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


