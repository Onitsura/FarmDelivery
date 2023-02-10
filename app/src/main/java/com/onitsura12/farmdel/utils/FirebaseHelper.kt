package com.onitsura12.farmdel.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.onitsura12.farmdel.domain.models.User

lateinit var AUTH: FirebaseAuth
lateinit var UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var USER: User


class FirebaseHelper {


    companion object {


        const val COURIER_GO_TO_TITLE = "Курьер выехал к вам"
        const val COURIER_GO_TO_MESSAGE = "в течение 1 - 1.5 часов будем у вас"
        const val NEW_ORDER_TITLE = "Новый заказ создан"
        const val NEW_ORDER_MESSAGE = ""
        const val ADMIN_TOPIC = "/adminTopic"
        const val BASE_URL = "https://fcm.googleapis.com"
        const val SERVER_KEY = "AAAA_7FJ4zo:APA91bHpnexK9iVL29Lw9UJSaB6UNbxPIFed-R3PbIKyYX3ozTkK4fQhNFhAaQ1zG769zwiD3z5e6DwNkrMBu7WOyKGMTYRFWhp1RtrezUeaYFxvXh3Gtg8tXeY7o5UqNudLdZHqN5Pm"
        const val CONTENT_TYPE = "application/json"
        const val NODE_WHITELIST = "whitelist"
        const val NODE_SUPPLIES = "supplies"
        const val NODE_USERS = "users"
        const val NODE_ORDERS = "orders"
        const val NODE_ORDERS_TO_DELIVERY = "ordersToDelivery"
        const val CHILD_TOKEN = "token"
        const val CHILD_ID = "id"
        const val CHILD_PHONE = "phone"
        const val CHILD_FULLNAME = "fullname"
        const val CHILD_EMAIL = "e-mail"
        const val CHILD_PHOTO = "photoUrl"
        const val CHILD_ADDRESS = "address"
        const val CHILD_CART = "cart"
        const val CHILD_CART_TITLE = "title"
        const val CHILD_CART_COST = "cost"
        const val CHILD_CART_COUNT = "count"
        const val CHILD_CART_WEIGHT = "weight"
        const val CHILD_CART_IMAGE_PATH = "imagePath"
        const val CHILD_CART_DELIVERY_DATE = "deliveryDate"
        const val CHILD_CART_DESCRIPTION = "description"
        const val CHILD_CART_IMAGES_ARRAY = "imagesArray"
        const val CHILD_CART_ADDITIONAL_SERVICES = "additionalServices"
        const val CHILD_CART_ADDITIONAL_SERVICES_IS_ADDED = "added"
        const val CHILD_CART_MIN_COUNT = "minCountValue"
        const val CHILD_CART_MIN_COUNT_IS_TRUE = "minCount"
        const val CHILD_ADDRESS_CITY = "city"
        const val CHILD_ADDRESS_STREET = "street"
        const val CHILD_ADDRESS_HOUSE = "house"
        const val CHILD_ADDRESS_ENTRANCE = "entrance"
        const val CHILD_ADDRESS_FLOOR = "floor"
        const val CHILD_ADDRESS_FLAT = "flat"
        const val CHILD_ADDRESS_ID = "id"
        const val CHILD_ADDRESS_PRIMARY = "primary"
        const val CHILD_ORDER_ID = "id"
        const val CHILD_ORDER_NUMBER = "number"
        const val CHILD_ORDER_ITEMS = "items"
        const val CHILD_ORDER_ADDRESS = "address"
        const val CHILD_ORDER_USER_PHONE = "userPhone"
        const val CHILD_ORDER_USER_NAME = "userName"
        const val CHILD_ORDER_AMOUNT = "amount"
        const val CHILD_ORDER_DELIVERY_DATE = "deliveryDate"
        const val CHILD_ORDER_PLACED_DATE = "placed"
        const val CHILD_SUPPLY_TITLE = "title"
        const val CHILD_SUPPLY_COST = "cost"
        const val CHILD_SUPPLY_COUNT = "count"
        const val CHILD_SUPPLY_WEIGHT = "weight"
        const val CHILD_SUPPLY_IMAGE_PATH = "imagePath"
        const val CHILD_SUPPLY_DELIVERY_DATE = "deliveryDate"








        fun initFirebase() {
            AUTH = FirebaseAuth.getInstance()
            REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
            USER = User()
            UID = AUTH.currentUser?.uid.toString()

        }


        fun initUser() {
            val uid = AUTH.currentUser?.uid.toString()
            val dataMap = mutableMapOf<String, Any?>()
            dataMap[CHILD_ID] = uid
            dataMap[CHILD_FULLNAME] = AUTH.currentUser?.displayName
            dataMap[CHILD_EMAIL] = AUTH.currentUser?.email

            REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        USER.fullname = AUTH.currentUser?.displayName
                        USER.eMail = AUTH.currentUser?.email
                        Log.i("init", USER.toString())
                    }

                }
        }
    }
}