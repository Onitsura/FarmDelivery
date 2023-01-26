package com.onitsura12.farmdel.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.domain.models.Order
import com.onitsura12.farmdel.databinding.OrderItemBinding


class OrderAdapter : ListAdapter<Order, OrderAdapter.ItemHolder>(ItemComparator()) {


    class ItemHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            val adapter = OrderItemAdapter(root = null, click = null, clickRemove = null)
            binding.apply {
                tvOrderCost.text = order.amount.toString()
                tvOrderItems.text = order.items.size.toString()
                tvOrderNumber.text = order.number
                tvOrderPlaced.text = order.placed.toString()
                orderItemRcView.adapter = adapter
                adapter.submitList(order.items)
            }
        }


        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    OrderItemBinding.inflate(
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
        holder.bind(getItem(position))
    }

    class ItemComparator : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }
}