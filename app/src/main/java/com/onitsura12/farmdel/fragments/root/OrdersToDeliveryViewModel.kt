package com.onitsura12.farmdel.fragments.root

import android.util.Log
import androidx.core.view.get
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_ORDERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_ORDERS_TO_DELIVERY
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.domain.models.Order
import dagger.hilt.android.lifecycle.HiltViewModel
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
                    Log.i("sorted", sortedList.toString())
                }
            }
        }
        _adapterList.value = sortedList
    }


}