package com.onitsura12.farmdel.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.ShopItemBinding
import com.squareup.picasso.Picasso

class ShopAdapter : ListAdapter<ShopItem, ShopAdapter.ItemHolder>(ItemComparator()) {


    class ItemHolder(private val binding: ShopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shopItem: ShopItem) {
            binding.apply {
                itemTitle.text = shopItem.title
                itemCost.text = shopItem.cost
                cartItemCounter.text = shopItem.count
                if (shopItem.imagePath != null && shopItem.imagePath!!.isNotEmpty() && shopItem
                        .imagePath!!.isNotBlank()) {
                    Picasso.get()
                        .load(shopItem.imagePath)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .fit()
                        .into(ivItem)
                }

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

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    ShopItemBinding.inflate(
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


    class ItemComparator : DiffUtil.ItemCallback<ShopItem>() {
        override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem == newItem
        }

    }
}
