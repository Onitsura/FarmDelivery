package com.onitsura12.farmdel.fragments.cart


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.AUTH
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_ADDITIONAL_SERVICES
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_ADDITIONAL_SERVICES_IS_ADDED
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_COST
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_COUNT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_DELIVERY_DATE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_IMAGE_PATH
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_TITLE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART_WEIGHT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.initUser
import com.onitsura12.data.storage.firebase.utils.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.UID
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.utils.LoginUtils.Companion.setupAccInfo


class CartViewModel : ViewModel() {
    private val _cart: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val cart: LiveData<ArrayList<ShopItem>> = _cart
    private val _totalCost: MutableLiveData<Int> = MutableLiveData()
    val totalCost: LiveData<Int> = _totalCost


    init {
//        UID = AUTH.currentUser?.uid.toString()
//        initUser()
//        setupAccInfo()
        initCart()
        checkTitles()


    }


    private fun initCart() {
        val list = arrayListOf<ShopItem>()
        var itemsTotalCost: Int

        _totalCost.value = 0
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_CART)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    list.clear()
                    for (cartSnapshot in snapshot.children) {
                        val cartItem = cartSnapshot.getValue(ShopItem::class.java)
                        list.add(cartItem!!)

                    }

                    _cart.value = list
                    itemsTotalCost = 0
                    for (i in list.indices) {
                        var itemAdditionalServices = 0
                        if (list[i].additionalServices != null) {
                            if (list[i].additionalServices!!.isAdded) itemAdditionalServices =
                                list[i].additionalServices!!.price.toInt()

                        }
                        val itemCost = list[i].cost.toInt()
                        val itemWeight = list[i].weight!!.toDouble()
                        val itemCount = list[i].count!!.toInt()
                        val itemAmount: Double = ((itemAdditionalServices * itemCount) +
                                (itemCost * itemCount * itemWeight))

                        itemsTotalCost += itemAmount.toInt()

                    }
                    _totalCost.value = itemsTotalCost
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }


    private fun checkTitles() {
        val list = arrayListOf<ShopItem>()
        REF_DATABASE_ROOT
            .child(NODE_SUPPLIES)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (cartSnapshot in snapshot.children) {
                        val cartItem = cartSnapshot.getValue(ShopItem::class.java)
                        list.add(cartItem!!)

                    }
                    if (_cart.value != null) {
                        val removeList = arrayListOf<ShopItem>()
                        if (list.size != _cart.value!!.size) {

                            for (i in _cart.value!!.indices) {
                                var match = false
                                for (j in list.indices) {
                                    if (_cart.value!![i].title == list[j].title) {
                                        match = true
                                    }

                                }


                                if (!match) {
                                    removeList.add(_cart.value!![i])
                                    removeFromCart(_cart.value!![i])
                                }
                            }
                        }
                        _cart.value!!.removeAll(removeList.toSet())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    private fun removeFromCart(cartItem: ShopItem) {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_CART)
            .child(cartItem.title).removeValue()
    }


    private fun addNewCartItem(cartItem: ShopItem) {

        val dataMap = mutableMapOf<String, Any?>()
        dataMap[CHILD_CART_IMAGE_PATH] = cartItem.imagePath
        dataMap[CHILD_CART_COUNT] = cartItem.count
        dataMap[CHILD_CART_TITLE] = cartItem.title
        dataMap[CHILD_CART_COST] = cartItem.cost
        dataMap[CHILD_CART_WEIGHT] = cartItem.weight
        dataMap[CHILD_CART_DELIVERY_DATE] = cartItem.deliveryDate
        dataMap[CHILD_CART_ADDITIONAL_SERVICES] = cartItem.additionalServices


        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_CART)
            .child(cartItem.title)
            .updateChildren(dataMap)
            .addOnCompleteListener {

            }


    }

    fun addAdditionalServices(cartItem: ShopItem) {
        if (cartItem.additionalServices != null) {
            val newValue = !cartItem.additionalServices!!.isAdded
            cartItem.additionalServices!!.isAdded = newValue
            REF_DATABASE_ROOT.child(NODE_USERS)
                .child(UID)
                .child(CHILD_CART)
                .child(cartItem.title)
                .child(CHILD_CART_ADDITIONAL_SERVICES)
                .child(CHILD_CART_ADDITIONAL_SERVICES_IS_ADDED).setValue(newValue)
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


