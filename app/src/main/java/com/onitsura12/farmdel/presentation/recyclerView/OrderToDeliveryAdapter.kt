package com.onitsura12.farmdel.presentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.farmdel.databinding.OrderToDeliveryItemBinding
import com.onitsura12.farmdel.domain.models.Order

class OrderToDeliveryAdapter(
    private val clickGoTo: (order: Order) -> Unit,
    private val clickComplete: (order: Order) -> Unit
) : ListAdapter<Order,
        OrderToDeliveryAdapter.ItemHolder>
    (ItemComparator()) {


    class ItemHolder(private val binding: OrderToDeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            order: Order,
            clickGoTo: (order: Order) -> Unit,
            clickComplete: (order: Order) -> Unit
        ) {


            val adapter = OrderItemAdapter(
                root = null,
                click = null,
                viewType = 2,
                clickRemove = null,
                clickAdditional = null
            )
            binding.rcViewOrdersToDelivery.adapter = adapter
            adapter.submitList(order.items)

            binding.apply {
                tvName.text = order.userName
                tvCity.text = order.address!!.city
                tvStreet.text = order.address.street
                tvEntrance.text = order.address.entrance
                tvHouse.text = order.address.house
                tvFloor.text = order.address.floor
                tvFlat.text = order.address.flat
                tvPhone.text = order.userPhone

                goToButton.setOnClickListener {
                    clickGoTo.invoke(order)
                    goToButton.visibility = View.GONE
                    completeButton.visibility = View.VISIBLE
                }
                completeButton.setOnClickListener {
                    clickComplete.invoke(order)
                }
            }
        }


        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    OrderToDeliveryItemBinding.inflate(
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
        holder.bind(getItem(position), clickGoTo = clickGoTo, clickComplete = clickComplete)
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