package com.onitsura12.data

import com.onitsura12.data.models.*
import com.onitsura12.domain.models.*

class Mapper {

    companion object {

        fun userToDomain(storageUserModel: StorageUserModel): User {
            return User(
                fullname = storageUserModel.name,
                lastName = storageUserModel.lastName,
                phone = storageUserModel.phone,
                eMail = storageUserModel.eMail,
                orders = storageUserModel.orders,
                address = storageUserModel.address,
                cart = storageUserModel.cart
            )
        }

        fun userToData(user: User): StorageUserModel {
            return StorageUserModel(
                name = user.fullname,
                lastName = user.lastName,
                phone = user.phone,
                eMail = user.eMail,
                orders = user.orders,
                address = user.address,
                cart = user.cart
            )

        }

//        fun orderToDomain(order: StorageOrder): Order {
//            val list = arrayListOf<OrderItem>()
//            for (item in order.items){
//                list.add(orderItemToDomain(item))
//            }
//            return Order(
//                number = order.number,
//                items = list,
//                amount = order.amount
//            )
//        }

//        fun orderToData(order: Order): StorageOrder{
//            val list = arrayListOf<StorageOrderItem>()
//            for (item in order.items){
//                list.add(orderItemToData(item))
//            }
//            return StorageOrder(
//                number = order.number,
//                items = list,
//                amount = order.amount
//            )
//        }

        private fun orderItemToDomain(item: StorageOrderItem): OrderItem{
            return OrderItem(
                title = item.title,
                count = item.count,
                price = item.price,
                deliveryDate = null
            )
        }

        private fun orderItemToData(item: OrderItem): StorageOrderItem{
            return StorageOrderItem(
                title = item.title,
                count = item.count,
                price = item.price
            )
        }

        fun shopItemToDomain(storageShopItem: StorageShopItem): ShopItem {
            return ShopItem(
                title = storageShopItem.title,
                cost = storageShopItem.cost,
                count = storageShopItem.count,
                weight = storageShopItem.weight,
                imagePath = storageShopItem.imagePath
            )

        }

        fun shopItemToData(shopItem: ShopItem): StorageShopItem {
            return StorageShopItem(
                title = shopItem.title,
                cost = shopItem.cost,
                count = shopItem.count,
                weight = shopItem.weight,
                imagePath = shopItem.imagePath
            )
        }

        fun addressToData(address: Address): StorageAddress{
            return StorageAddress(
                city = address.city,
                street = address.street,
                house = address.house,
                entrance = address.entrance,
                floor = address.floor,
                flat = address.flat,
                id = address.id
            )
        }

        fun addressToDomain(storageAddress: StorageAddress): Address{
            return Address(
                city = storageAddress.city,
                street = storageAddress.street,
                house = storageAddress.house,
                entrance = storageAddress.entrance,
                floor = storageAddress.floor,
                flat = storageAddress.flat,
                id = storageAddress.id
            )
        }

        fun cartToData(cart: ArrayList<ShopItem>): ArrayList<StorageShopItem>{
            val list = arrayListOf<StorageShopItem>()
            for (i in cart.indices){
                list.add(shopItemToData(cart[i]))
            }
            return list
        }

        fun cartToDomain(storageCart: ArrayList<StorageShopItem>): ArrayList<ShopItem>{
                val list = arrayListOf<ShopItem>()
                for (i in storageCart.indices){
                    list.add(shopItemToDomain(storageCart[i]))
                }
                return list
            }


    }
}