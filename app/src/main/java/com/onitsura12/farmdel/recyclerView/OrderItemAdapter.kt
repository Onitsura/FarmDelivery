package com.onitsura12.farmdel.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.databinding.OrderDetailsItemBinding

class OrderItemAdapter(
    private val root: Boolean?,
    private val click: ((orderItem: ShopItem) -> Unit)?
) :
    ListAdapter<ShopItem,
            OrderItemAdapter
            .ItemHolder>
        (ItemComparator()) {


    class ItemHolder(private val binding: OrderDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(orderItem: ShopItem, root: Boolean?, click: ((orderItem: ShopItem) -> Unit)?) {
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
                        saveButton.visibility = View.VISIBLE

                        saveButton.setOnClickListener {
                            orderItem.deliveryDate = etChangeDate.text.toString()
                            orderItem.weight = etChangeWeight.text.toString()
                            orderItem.title = etChangeTitle.text.toString()
                            orderItem.cost = etChangeCost.text.toString()

                            click!!.invoke(orderItem)
                        }

                    }
                } else {
                    if (orderItem.count!!.toInt() > 0) {
                        tvOrderItemName.text = orderItem.title
                        tvOrderItemCounter.text = orderItem.count
                    } else {
                        tvOrderItemName.visibility = View.GONE
                        tvOrderItemCounter.visibility = View.GONE
                        tvOrderItemCount.visibility = View.GONE
                    }
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
        holder.bind(getItem(position), root = root, click = click)
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
