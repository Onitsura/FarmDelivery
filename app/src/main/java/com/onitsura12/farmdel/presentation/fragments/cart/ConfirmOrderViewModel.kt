package com.onitsura12.farmdel.presentation.fragments.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.farmdel.domain.models.Address
import com.onitsura12.farmdel.domain.models.Order
import com.onitsura12.farmdel.domain.models.ShopItem
import com.onitsura12.farmdel.notification.NotificationData
import com.onitsura12.farmdel.notification.PushNotification
import com.onitsura12.farmdel.notification.PushService
import com.onitsura12.farmdel.notification.RetrofitInstance
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.ADMIN_TOPIC
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_ADDRESS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_ADDRESS_PRIMARY
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_CART
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_TOKEN
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NEW_ORDER_MESSAGE
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NEW_ORDER_TITLE
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_ORDERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.UID
import com.onitsura12.farmdel.utils.USER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
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
    private val _totalCost: MutableLiveData<Int> = MutableLiveData()
    val totalCost: LiveData<Int> = _totalCost

    init {
        initAddressList()
        initOrderItemsList()
        getNumber()
        checkPhone()
    }

    fun createNotification() {
        PushNotification(
            NotificationData(title = NEW_ORDER_TITLE, message = NEW_ORDER_MESSAGE),
            ADMIN_TOPIC
        ).also { sendNotificationToAdmin(it) }
    }

    private fun sendNotificationToAdmin(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = RetrofitInstance.api.postNotification(notification = notification)
                Log.i("notif", notification.toString())

                if (!response.isSuccessful) {
                    Log.e("sendNotification", response.errorBody().toString())
                }

            } catch (e: Exception) {
                Log.e("sendNotification", e.toString())
            }
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
        var itemsTotalCost: Int
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
                    getAdapterList(_orderItemsList.value!!)
                    checkOrder()

                    itemsTotalCost = 0
                    for (i in list.indices) {

                        val itemCost: Double = list[i].cost.toInt() * list[i].weight!!.toDouble() *
                                list[i].count!!.toInt()
                        itemsTotalCost += itemCost.toInt()

                    }
                    _totalCost.value = itemsTotalCost
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }


    private fun getAdapterList(list: ArrayList<ShopItem>) {
        val removeList = arrayListOf<ShopItem>()
        for (i in list.indices) {
            if (list[i].count!!.toInt() == 0) {
                removeList.add(list[i])
            }
        }
        list.removeAll(removeList.toSet())
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

        val currentDate = now.time
        val targetFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy 'в' HH:mm", Locale.ROOT)
        val placedDate: String = targetFormat.format(currentDate)

        val newOrder = Order(
            id = getId(_number.value.toString()),
            number = _number.value.toString(),
            items = items,
            address = _addressList.value!![0],
            userPhone = USER.phone,
            userName = USER.fullname,
            placed = placedDate,
            token = PushService.token
        )
        USER.token = PushService.token



        REF_DATABASE_ROOT.child(NODE_ORDERS).child(newOrder.id).setValue(newOrder)
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(NODE_ORDERS).child(newOrder.id)
            .setValue(newOrder)
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_TOKEN).setValue(USER.token)

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