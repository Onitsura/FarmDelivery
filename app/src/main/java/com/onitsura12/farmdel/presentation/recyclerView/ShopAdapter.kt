package com.onitsura12.farmdel.presentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.ShopItemBinding
import com.onitsura12.farmdel.domain.models.ShopItem
import com.squareup.picasso.Picasso

class ShopAdapter(
    val clickIncrement: (cartItem: ShopItem) -> Unit,
    val clickDecrement: (cartItem: ShopItem) -> Unit,
    val chooseClick: ((shopItem: ShopItem) -> Unit)?
) :
    ListAdapter<ShopItem, ShopAdapter.ItemHolder>(ItemComparator()) {


    class ItemHolder(private val binding: ShopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(
            shopItem: ShopItem,
            clickIncrement: (cartItem: ShopItem) -> Unit,
            clickDecrement: (cartItem: ShopItem) -> Unit,
            chooseClick: ((shopItem: ShopItem) -> Unit)?
        ) {
            val itemWeightText = "Примерный вес ${shopItem.weight} кг"
            val minCountText = "Минимальный заказ от ${shopItem.minCountValue} шт"
            val itemCostText = "${shopItem.cost} рублей за кг"

            binding.apply {
                if (shopItem.minCount!!) {
                    tvMinCount.visibility = View.VISIBLE
                    tvMinCount.text = minCountText
                }

                itemWeight.text = itemWeightText
                itemTitle.text = shopItem.title
                itemCost.text = itemCostText
                cartItemCounter.text = shopItem.count.toString()
                if (shopItem.imagePath != null && shopItem.imagePath!!.isNotEmpty() && shopItem
                        .imagePath!!.isNotBlank()
                ) {
                    Picasso.get()
                        .load(shopItem.imagePath)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .centerCrop()
                        .fit()
                        .into(ivItem)
                }
                itemView.setOnClickListener {
                    chooseClick!!.invoke(shopItem)
                }
                cartItemDecreaseButton.setOnClickListener {
                    clickDecrement.invoke(shopItem)
                    if (cartItemCounter.text!!.toString().toInt() > 0) {
                        val newValue = cartItemCounter.text!!.toString().toInt() - 1
                        cartItemCounter.text = newValue.toString()

                    }


                }
                cartItemIncreaseButton.setOnClickListener {
                    clickIncrement.invoke(shopItem)
                    val newValue = cartItemCounter.text!!.toString().toInt() + 1
                    cartItemCounter.text = newValue.toString()

                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        return ItemHolder(
            ShopItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(
            getItem(position), clickIncrement = clickIncrement,
            clickDecrement = clickDecrement, chooseClick = chooseClick
        )
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
