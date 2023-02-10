package com.onitsura12.farmdel.presentation.fragments.shop



import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.onitsura12.farmdel.domain.models.ShopItem
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.ADMIN_TOPIC
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_ADDITIONAL_SERVICES
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_COST
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_COUNT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_DELIVERY_DATE
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_DESCRIPTION
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_IMAGES_ARRAY
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_IMAGE_PATH
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_MIN_COUNT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_MIN_COUNT_IS_TRUE
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_TITLE
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART_WEIGHT
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_WHITELIST
import com.onitsura12.farmdel.utils.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.UID
import com.onitsura12.farmdel.utils.USER


class ShopViewModel : ViewModel() {

    private val shopItemList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    private val _showAddButton: MutableLiveData<Boolean> = MutableLiveData()
    val showAddButton: LiveData<Boolean> = _showAddButton
    val isCartEmpty: MutableLiveData<Boolean> = MutableLiveData()
    private val _cart: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val cart: LiveData<ArrayList<ShopItem>> = _cart
    private val _adapterList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val adapterList: LiveData<ArrayList<ShopItem>> = _adapterList
    var counter = 0


    init {
        isCartEmpty.value = true
        initSuppliesList()
        getCart()
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
                    shopItemList.value = list
                    _adapterList.value = list
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
                    list.clear()
                    counter = 0
                    for (cartSnapshot in snapshot.children) {
                        val cartItem = cartSnapshot.getValue(ShopItem::class.java)
                        list.add(cartItem!!)
                        counter += cartItem.count!!.toInt()
                        isCartEmpty.value = counter <= 0
                    }
                    _cart.value = list
                    USER.cart = list
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
                    list.clear()
                    for (supplySnapshot in snapshot.children) {
                        val shopItem = supplySnapshot.getValue(ShopItem::class.java)
                        list.add(shopItem!!)
                        Log.i("shopItem", shopItem.toString())
                    }
                    for (i in list.indices){
                        Log.i("listItem", list[i].toString())
                    }

                    for (i in _cart.value!!.indices) {
                        for (y in list.indices) {
                            if (list[y].title == _cart.value!![i].title) {
                                if (_cart.value!![i].count!!.toInt() > list[y].count!!.toInt()) {
                                    list[y].count = _cart.value!![i].count
                                }
                                if (_cart.value!![i].additionalServices?.isAdded != list[y].additionalServices?.isAdded){
                                    list[y].additionalServices?.isAdded = _cart.value!![i].additionalServices?.isAdded == true
                                }
                            }
                        }
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
                    checkTitles()


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun updateAdapterList() {
        for (y in _cart.value!!.indices) {
            for (i in _adapterList.value!!.indices) {
                if (_cart.value!![y].title == _adapterList.value!![i].title) {
                    if (_cart.value!![y].count!!.toInt() > _adapterList.value!![i]
                            .count!!.toInt()
                    ) {

                        _adapterList.value = _cart.value
                        Log.i("adapter", _cart.value.toString())
                    }
                }
            }
        }
    }
    private fun checkTitles(){
        val list = arrayListOf<ShopItem>()
        REF_DATABASE_ROOT
            .child(NODE_SUPPLIES)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for(shopSnapshot in snapshot.children){
                        val shopItem = shopSnapshot.getValue(ShopItem::class.java)
                        list.add(shopItem!!)

                    }
                    val removeList = arrayListOf<ShopItem>()
                    if (list.size != _cart.value!!.size){

                        for (i in _cart.value!!.indices) {
                            var match = false
                            for (j in list.indices) {
                                if (_cart.value!![i].title == list[j].title){
                                    match = true
                                }
                            }

                            if (!match){
                                removeList.add(_cart.value!![i])
                            }
                        }
                    }
                    _cart.value!!.removeAll(removeList.toSet())
                    updateAdapterList()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }


    private fun addNewCartItem(cartItem: ShopItem) {

        val dataMap = mutableMapOf<String, Any?>()
        dataMap[CHILD_CART_IMAGE_PATH] = cartItem.imagePath
        dataMap[CHILD_CART_COUNT] = cartItem.count
        dataMap[CHILD_CART_TITLE] = cartItem.title
        dataMap[CHILD_CART_COST] = cartItem.cost
        dataMap[CHILD_CART_WEIGHT] = cartItem.weight
        dataMap[CHILD_CART_DELIVERY_DATE] = cartItem.deliveryDate
        dataMap[CHILD_CART_DESCRIPTION] = cartItem.description
        dataMap[CHILD_CART_IMAGES_ARRAY] = cartItem.imagesArray
        dataMap[CHILD_CART_ADDITIONAL_SERVICES] = cartItem.additionalServices
        dataMap[CHILD_CART_MIN_COUNT] = cartItem.minCountValue
        dataMap[CHILD_CART_MIN_COUNT_IS_TRUE] = cartItem.minCount


        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_CART)
            .child(cartItem.title)
            .updateChildren(dataMap)
            .addOnCompleteListener {

            }


    }

    private fun updateCartCount(cartItem: ShopItem) {

        val dataMap = mutableMapOf<String, Any?>()
        dataMap[CHILD_CART_COUNT] = cartItem.count

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
        updateCartCount(cartItem)
        counter += 1
        isCartEmpty.value = counter <= 0
    }

    fun decrementItemCount(cartItem: ShopItem) {
        if (cartItem.count?.toInt()!! > 0) {
            val newValue = cartItem.count?.toInt()?.minus(1)
            cartItem.count = newValue.toString()
            updateCartCount(cartItem)
            counter -= 1
            isCartEmpty.value = counter <= 0
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
                            FirebaseMessaging.getInstance().subscribeToTopic(ADMIN_TOPIC)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }

}


