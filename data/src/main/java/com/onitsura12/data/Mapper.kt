package com.onitsura12.data

import com.onitsura12.data.models.StorageOrder
import com.onitsura12.data.models.StorageOrderItem
import com.onitsura12.data.models.StorageShopItemModel
import com.onitsura12.data.models.StorageUserModel
import com.onitsura12.domain.models.Order
import com.onitsura12.domain.models.OrderItem
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.domain.models.User

class Mapper {

    companion object {

        fun userToDomain(storageUserModel: StorageUserModel): User {
            return User(
                name = storageUserModel.name,
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
                name = user.name,
                lastName = user.lastName,
                phone = user.phone,
                eMail = user.eMail,
                orders = user.orders,
                address = user.address,
                cart = user.cart
            )

        }

        fun orderToDomain(order: StorageOrder): Order {
            val list = arrayListOf<OrderItem>()
            for (item in order.items){
                list.add(orderItemToDomain(item))
            }
            return Order(
                number = order.number,
                items = list,
                amount = order.amount
            )
        }

        fun orderToData(order: Order): StorageOrder{
            val list = arrayListOf<StorageOrderItem>()
            for (item in order.items){
                list.add(orderItemToData(item))
            }
            return StorageOrder(
                number = order.number,
                items = list,
                amount = order.amount
            )
        }

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

        fun shopItemToDomain(storageShopItemModel: StorageShopItemModel): ShopItem {
            return ShopItem(
                title = storageShopItemModel.title,
                cost = storageShopItemModel.cost,
                count = storageShopItemModel.count,
                weight = storageShopItemModel.weight,
                imagePath = storageShopItemModel.imagePath
            )

        }

        fun shopItemToData(shopItem: ShopItem): StorageShopItemModel {
            return StorageShopItemModel(
                title = shopItem.title,
                cost = shopItem.cost,
                count = shopItem.count,
                weight = shopItem.weight,
                imagePath = shopItem.imagePath
            )
        }

    }
}