package com.onitsura12.farmdel.fragments.root

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.COURIER_GO_TO_MESSAGE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.COURIER_GO_TO_TITLE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NEW_ORDER_MESSAGE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NEW_ORDER_TITLE
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_ORDERS
import com.onitsura12.data.storage.firebase.utils.REF_DATABASE_ROOT
import com.onitsura12.domain.models.Order
import com.onitsura12.farmdel.utils.notification.NotificationData
import com.onitsura12.farmdel.utils.notification.PushNotification
import com.onitsura12.farmdel.utils.notification.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersToDeliveryViewModel @Inject constructor() : ViewModel() {
    private val _ordersList: MutableLiveData<ArrayList<Order>> = MutableLiveData()
    val ordersList: LiveData<ArrayList<Order>> = _ordersList
    private val _adapterList: MutableLiveData<ArrayList<Order>> = MutableLiveData()
    val adapterList: LiveData<ArrayList<Order>> = _adapterList
    val allDates: MutableLiveData<ArrayList<String>> = MutableLiveData()


    init {

    }

     fun getOrders() {
        val list: ArrayList<Order> = arrayListOf()
        REF_DATABASE_ROOT
            .child(NODE_ORDERS)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for (orderSnapshot in snapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        list.add(order!!)
                    }
                    _ordersList.value = list
                    _adapterList.value = list
                    getAllDates()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun getAllDates(){
        val list: ArrayList<Order> = _ordersList.value!!
        val listOfDates = arrayListOf<String>()
        for (i in list.indices){
            for (j in list[i].items.indices){
                if (!listOfDates.contains(list[i].items[j].deliveryDate.toString())) {
                    listOfDates.add(list[i].items[j].deliveryDate.toString())
                }
            }
        }
        allDates.value = listOfDates
    }





    fun findOrderByDate(date: String){
        val list: ArrayList<Order> = _ordersList.value!!
        val sortedList: ArrayList<Order> = arrayListOf()
        for (i in list.indices){
            for (j in list[i].items.indices){
                if (list[i].items[j].deliveryDate!!.contains(date)){
                    sortedList.add(list[i])
                }
            }
        }
        _adapterList.value = sortedList
    }



    fun finishDelivery(order: Order){
        val list = _adapterList.value!!
        if (list.contains(order)){
            list.remove(order)
            _adapterList.value = list
        }
    }


    fun createNotification(order: Order){
            PushNotification(
                NotificationData(title = COURIER_GO_TO_TITLE, message = COURIER_GO_TO_MESSAGE),
                order.token.toString()
            ).also { sendNotificationGoTo(it) }

    }

    private fun sendNotificationGoTo(notification: PushNotification) = CoroutineScope(Dispatchers
        .IO).launch {
        try {

            val response = RetrofitInstance.api.postNotification(notification = notification)

            if (response.isSuccessful){

            }
            else Log.e("sendNotification", response.errorBody().toString())

        }
        catch (e: Exception){
            Log.e("sendNotification", e.toString())
        }
    }

}