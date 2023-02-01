package com.onitsura12.farmdel.presentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.farmdel.databinding.OrderDetailsItemBinding
import com.onitsura12.farmdel.domain.models.ShopItem

class OrderItemAdapter(
    private val root: Boolean?,
    private val click: ((orderItem: ShopItem) -> Unit)?,
    private val clickRemove: ((shopItem: ShopItem) -> Unit)?,
    private val viewType: Int?,
    private val clickAdditional: ((orderItem: ShopItem) -> Unit)?
) :
    ListAdapter<ShopItem,
            OrderItemAdapter.ItemHolder>
        (ItemComparator()) {


    class ItemHolder(private val binding: OrderDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(
            orderItem: ShopItem,
            root: Boolean?,
            viewType: Int?,
            click: ((orderItem: ShopItem) -> Unit)?,
            clickRemove: ((shopItem: ShopItem) -> Unit)?,
            clickAdditional: ((orderItem: ShopItem) -> Unit)?
        ) {
            binding.apply {
                if (root != null) {
                    if (!root) {
                        if (orderItem.count!!.toInt() > 0) {

                            tvOrderItemName.text = orderItem.title
                            tvOrderItemCounter.text = orderItem.count
                        } else {
                            tvOrderItemName.visibility = View.GONE
                            tvOrderItemCounter.visibility = View.GONE
                            tvOrderItemCount.visibility = View.GONE
                        }
                    } else {
                        bindAdminPanel(
                            orderItem = orderItem,
                            click = click,
                            clickRemove = clickRemove
                        )

                    }
                } else {
                    if (orderItem.count!!.toInt() > 0) {
                        tvOrderItemName.text = orderItem.title
                        tvOrderItemCounter.text = orderItem.count

                        if (viewType == 2 && orderItem.additionalServices != null){
                            if (orderItem.additionalServices.isAdded) {
                                tvOrderItemName.text = orderItem.title
                                tvOrderItemCounter.text = orderItem.count
                                tvOrderItemCounter.text = orderItem.count

                                tvOrderAdditionalTitle.visibility = View.VISIBLE
                                tvOrderAdditionalTitle.text =
                                    orderItem.additionalServices.title.toString()
                            }

                        }

                    } else {
                        tvOrderItemName.visibility = View.GONE
                        tvOrderItemCounter.visibility = View.GONE
                        tvOrderItemCount.visibility = View.GONE
                    }
                }
                if (viewType == 1 && orderItem.additionalServices != null){
                    tvOrderItemCounter.visibility = View.GONE
                    tvOrderItemCount.visibility = View.GONE
                    tvOrderItemName.visibility = View.GONE


                    tvOrderAdditionalTitle.visibility = View.VISIBLE
                    tvRoubles.visibility = View.VISIBLE
                    tvOrderAdditionalTitle.text = orderItem.additionalServices.title
                    tvCost.visibility = View.VISIBLE
                    tvAdditionalPrice.visibility = View.VISIBLE
                    tvAdditionalPrice.text = orderItem.additionalServices.price
                    addAdditional.visibility = View.VISIBLE
                    addAdditional.isChecked = orderItem.additionalServices.isAdded

                    addAdditional.setOnClickListener {
                        if (clickAdditional != null) {
                            clickAdditional.invoke(orderItem)
                        }
                    }
                }

            }
        }

        private fun bindAdminPanel(
            orderItem: ShopItem,
            click: ((orderItem: ShopItem) -> Unit)?,
            clickRemove: ((shopItem: ShopItem) -> Unit)?
        ) {
            binding.apply {
                tvOrderItemCounter.visibility = View.GONE
                tvOrderItemCount.visibility = View.GONE
                tvOrderItemName.visibility = View.GONE



                tvChangeCost.visibility = View.VISIBLE
                etChangeCost.visibility = View.VISIBLE
                etChangeCost.hint = orderItem.cost
                tvChangeTitle.visibility = View.VISIBLE
                etChangeTitle.visibility = View.VISIBLE
                etChangeTitle.hint = orderItem.title
                tvChangeWeight.visibility = View.VISIBLE
                etChangeWeight.visibility = View.VISIBLE
                etChangeWeight.hint = orderItem.weight
                tvChangeDeliveryDateSample.visibility = View.VISIBLE
                etChangeDate.visibility = View.VISIBLE
                etChangeDate.hint = orderItem.deliveryDate
                saveButton.visibility = View.VISIBLE

                saveButton.setOnClickListener {
                    if (etChangeCost.text.toString().isNotBlank()) {
                        orderItem.cost = etChangeCost.text.toString()
                    }
                    if (etChangeDate.text.toString().isNotBlank()) {
                        orderItem.deliveryDate = etChangeDate.text.toString()
                    }
                    if (etChangeWeight.text.toString().isNotBlank()) {
                        orderItem.weight = etChangeWeight.text.toString()
                    }
                    if (etChangeTitle.text.toString().isNotBlank()) {
                        orderItem.title = etChangeTitle.text.toString()
                    }

                    click!!.invoke(orderItem)
                }

                removeShopItemButton.visibility = View.VISIBLE
                removeShopItemButton.setOnClickListener {
                    clickRemove!!.invoke(orderItem)
                }
            }
        }


        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    OrderDetailsItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent = parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(
            getItem(position), root = root, click = click, clickRemove = clickRemove,
            viewType = viewType, clickAdditional = clickAdditional
        )
    }

    class ItemComparator : DiffUtil.ItemCallback<ShopItem>() {
        override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.count == newItem.count
        }

    }
}
