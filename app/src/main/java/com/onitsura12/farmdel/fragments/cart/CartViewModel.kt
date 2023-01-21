package com.onitsura12.farmdel.fragments.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.CHILD_CART
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.UID
import com.onitsura12.domain.models.ShopItem


class CartViewModel : ViewModel() {
    private val _cart: MutableLiveData<ArrayList<ShopItem>> = MutableLiveData()
    val cart: LiveData<ArrayList<ShopItem>> = _cart




    init {

        initCart()
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

                        Log.i("cartItem", cartSnapshot.value.toString())
                        list.add(cartItem!!)
                        _cart.value = list
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


    }


//    private fun getImageUrl(title: String){
//
//        REF_DATABASE_ROOT.child(FirebaseHelper.NODE_SUPPLIES).child(title).child("imagePath")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val imagePath = snapshot.value.toString()
//                    Log.i("imageIn", imagePath)
//                    imagePathUrl.value = imagePath
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//    }








}


