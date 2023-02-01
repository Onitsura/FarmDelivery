package com.onitsura12.farmdel.presentation.fragments.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.farmdel.domain.models.Order
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_ORDERS
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.UID
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Collections.swap
import javax.inject.Inject

@HiltViewModel
class AccOrdersViewModel @Inject constructor() : ViewModel() {
    private val _ordersList: MutableLiveData<ArrayList<Order>> = MutableLiveData()
    val ordersList: LiveData<ArrayList<Order>> = _ordersList


    init {
        getOrders()
    }

    private fun getOrders() {
        val list: ArrayList<Order> = arrayListOf()
        REF_DATABASE_ROOT
            .child(NODE_USERS)
            .child(UID)
            .child(NODE_ORDERS)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for (orderSnapshot in snapshot.children) {
                        val order = orderSnapshot.getValue(Order::class.java)
                        list.add(order!!)
                    }
                    for (left in 0 until list.size) {
                        var minInd = left
                        for (i in left until list.size) {
                            if (list[i].number.toInt() < list[minInd].number.toInt()) {
                                minInd = i
                            }
                        }
                        swap(list, left, minInd)
                    }




                        _ordersList.value = list


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


}