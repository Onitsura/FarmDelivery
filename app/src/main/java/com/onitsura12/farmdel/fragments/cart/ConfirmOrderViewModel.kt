package com.onitsura12.farmdel.fragments.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_ADDRESS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_ADDRESS_PRIMARY
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_ORDERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.USER
import com.onitsura12.domain.models.Address
import com.onitsura12.domain.models.Order
import com.onitsura12.domain.models.ShopItem
import java.util.*

class ConfirmOrderViewModel : ViewModel() {
    private val _addressList: MutableLiveData<ArrayList<Address>> = MutableLiveData()
    val addressList: LiveData<ArrayList<Address>> = _addressList
    private val _orderItemsList: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val orderItemsList: LiveData<ArrayList<ShopItem>> = _orderItemsList
    private val _number: MutableLiveData<String> = MutableLiveData()
    private val _isAddressNull: MutableLiveData<Boolean> = MutableLiveData()
    val isAddressNull: LiveData<Boolean> = _isAddressNull
    private val _isOrderListEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val isOrderListEmpty: LiveData<Boolean> = _isOrderListEmpty
    private val _isPhoneNull: MutableLiveData<Boolean> = MutableLiveData()
    val isPhoneNull: LiveData<Boolean> = _isPhoneNull

    init {
        initAddressList()
        initOrderItemsList()
        getNumber()
        checkPhone()
    }


    private fun initAddressList() {
        val list = arrayListOf<Address>()
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_ADDRESS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (addressSnapshot in snapshot.children) {
                        val address = addressSnapshot.getValue(Address::class.java)
                        if (address!!.primary) {
                            list.add(address)
                        }

                    }
                    _addressList.value = list
                    checkAddress()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun initOrderItemsList() {

        val list = arrayListOf<ShopItem>()
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

                    _orderItemsList.value = list
                    checkOrder()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }

    fun markAddress(address: Address) {
        val dataMap = mutableMapOf<String, Any?>()
        dataMap[CHILD_ADDRESS_PRIMARY] = !address.primary

        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_ADDRESS)
            .child(address.id)
            .updateChildren(dataMap)
            .addOnCompleteListener {
            }
    }

    private fun getNumber() {
        val ref = REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(NODE_ORDERS)
        if (ref.toString().isEmpty()) {
            _number.value = "1"
        } else {
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list: ArrayList<Order> = arrayListOf()
                    for (item in snapshot.children) {
                        val orderItem = item.getValue(Order::class.java)
                        if (orderItem != null) {
                            list.add(orderItem)
                        }
                    }
                    var maxNumber = 0
                    for (i in list.indices) {
                        if (list[i].number.toInt() >= maxNumber) {
                            maxNumber = list[i].number.toInt()
                        }
                    }
                    _number.value = (maxNumber + 1).toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    private fun getId(number: String): String {
        return (UID + number)
    }

    private fun checkOrder() {
        _isOrderListEmpty.value = true
        if (_orderItemsList.value != null) {
            for (i in _orderItemsList.value!!.indices) {
                if (_orderItemsList.value!![i].count!!.toInt() != 0) {
                    _isOrderListEmpty.value = false
                }
            }
        }
    }


    private fun checkAddress() {
        _isAddressNull.value = false
        if (_addressList.value != null) {
            if (_addressList.value!!.isEmpty()) {
                _isAddressNull.value = true
            }
        } else _isAddressNull.value = true
    }

    private fun checkPhone() {
        _isPhoneNull.value = true
        if (USER.phone.isNotEmpty() && USER.phone.isNotBlank()) {
            if (USER.phone.length == 10 || USER.phone.length == 11 || USER.phone.length == 12) {
                _isPhoneNull.value = false
            }
        }

    }

    fun createOrder() {

        val list = _orderItemsList.value
        val items = arrayListOf<ShopItem>()
        if (list != null) {
            for (i in list.indices) {
                if (list[i].count != "0") {
                    items.add(list[i])
                }
            }
        }
        val now = Calendar.getInstance()

        val placedDate = now.time

        val newOrder = Order(
            id = getId(_number.value.toString()),
            number = _number.value.toString(),
            items = items,
            address = _addressList.value!![0],
            userPhone = USER.phone,
            userName = USER.fullname,
            placed = placedDate
        )

        REF_DATABASE_ROOT.child(NODE_ORDERS).child(newOrder.id).setValue(newOrder)
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(NODE_ORDERS).child(newOrder.id)
            .setValue(newOrder)

    }

    fun cleanCart() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_CART).setValue("")
    }

    fun removeAddress(address: Address) {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_ADDRESS)
            .child(address.id)
            .removeValue()
            .addOnCompleteListener {
            }
    }

}