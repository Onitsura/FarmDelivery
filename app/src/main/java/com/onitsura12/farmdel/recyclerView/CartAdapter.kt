package com.onitsura12.farmdel.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.CartItemBinding
import com.squareup.picasso.Picasso

class CartAdapter : ListAdapter<ShopItem, CartAdapter.ItemHolder>(ItemComparator()) {


    class ItemHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(shopItem: ShopItem) {
            binding.apply {
                tvCartItemName.text = shopItem.title
                tvCartItemCost.text = shopItem.cost
                tvCartItemWeight.text = shopItem.weight
                tvDeliveryDate.text = shopItem.deliveryDate.toString()
                cartItemCounter.text = shopItem.count

                if (shopItem.imagePath != null && shopItem.imagePath!!.isNotEmpty() && shopItem
                        .imagePath!!.isNotBlank()
                ) {
                    Picasso.get()
                        .load(shopItem.imagePath)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .fit()
                        .into(ivPreview)

                    binding.cartItemDecreaseButton.setOnClickListener {
                        if ((cartItemCounter.text as String).toInt() > 0) {
                            val newValue = (cartItemCounter.text as String?)!!.toInt() - 1
                            cartItemCounter.text = newValue.toString()

                        }
                    }

                    binding.cartItemIncreaseButton.setOnClickListener {
                        val newValue = (cartItemCounter.text as String?)!!.toInt() + 1
                        cartItemCounter.text = newValue.toString()

                    }
                }
            }
        }


        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    CartItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    ))
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent = parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemComparator : DiffUtil.ItemCallback<ShopItem>() {
        override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem == newItem
        }

    }


}