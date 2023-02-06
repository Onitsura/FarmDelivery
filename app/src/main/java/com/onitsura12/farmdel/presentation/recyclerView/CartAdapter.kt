package com.onitsura12.farmdel.presentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.CartItemBinding
import com.onitsura12.farmdel.domain.models.ShopItem
import com.squareup.picasso.Picasso

class CartAdapter(
    private val clickIncrement: (cartItem: ShopItem) -> Unit,
    private val clickDecrement: (cartItem: ShopItem) -> Unit,
    private val clickAdditional: (cartItem: ShopItem) -> Unit
) :
    ListAdapter<ShopItem,
            CartAdapter.ItemHolder>(ItemComparator()) {


    class ItemHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(
            shopItem: ShopItem,
            clickIncrement: (cartItem: ShopItem) -> Unit,
            clickDecrement: (cartItem: ShopItem) -> Unit,
            clickAdditional: (cartItem: ShopItem) -> Unit
        ) {
            binding.apply {

                if (shopItem.count!!.toInt() > 0) {
                    showMinCountWarning(shopItem)
                    setupRcViewAdditional(shopItem = shopItem, clickAdditional = clickAdditional)

                    cartItemCounter.text = shopItem.count

                    tvCartItemName.text = shopItem.title
                    tvCartItemCost.text = shopItem.cost
                    tvCartItemWeight.text = shopItem.weight
                    tvDeliveryDate.text = shopItem.deliveryDate.toString()





                    if (shopItem.imagePath != null &&
                        shopItem.imagePath!!.isNotEmpty() &&
                        shopItem.imagePath!!.isNotBlank()
                    ) {
                        Picasso.get()
                            .load(shopItem.imagePath)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_background)
                            .centerCrop()
                            .fit()
                            .into(ivPreview)

                        binding.cartItemDecreaseButton.setOnClickListener {
                            clickDecrement.invoke(shopItem)
                            if ((cartItemCounter.text as String).toInt() > 0) {
                                val newValue = (cartItemCounter.text as String?)!!.toInt() - 1
                                cartItemCounter.text = newValue.toString()
                                showMinCountWarning(shopItem)
                            }
                        }

                        binding.cartItemIncreaseButton.setOnClickListener {
                            clickIncrement.invoke(shopItem)
                            val newValue = (cartItemCounter.text as String?)!!.toInt() + 1
                            cartItemCounter.text = newValue.toString()
                            showMinCountWarning(shopItem)
                        }
                    }
                } else {
                    tvDeliveryDateSample.visibility = View.GONE
                    ivPreview.visibility = View.GONE
                    tvCartItemName.visibility = View.GONE
                    tvCartItemCost.visibility = View.GONE
                    tvCartItemWeight.visibility = View.GONE
                    tvDeliveryDate.visibility = View.GONE
                    cartItemCounter.visibility = View.GONE
                    cartItemDecreaseButton.visibility = View.GONE
                    cartItemIncreaseButton.visibility = View.GONE
                    tvCostPer.visibility = View.GONE
                    itemAverageWeight.visibility = View.GONE
                    itemWeightMeasurement.visibility = View.GONE
                }
            }

        }

        private fun setupRcViewAdditional(
            shopItem: ShopItem,
            clickAdditional: (cartItem: ShopItem) -> Unit
        ) {
            if (shopItem.additionalServices != null) {
                binding.additionalServicesRcView.visibility = View.VISIBLE
                val adapter = OrderItemAdapter(
                    root = null,
                    click = null,
                    viewType = 1,
                    clickRemove = null,
                    clickAdditional = clickAdditional
                )
                binding.additionalServicesRcView.adapter = adapter
                adapter.submitList(listOf(shopItem))
            }
        }

        private fun showMinCountWarning(shopItem: ShopItem, ) {
            binding.apply {
                if (shopItem.minCountValue != "") {
                    if (shopItem.count!!.toInt() >= shopItem.minCountValue!!.toInt()) {

                        tvMinCount.visibility = View.GONE
                    } else tvMinCount.visibility = View.VISIBLE
                }
            }
        }


        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    CartItemBinding.inflate(
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
            getItem(position), clickDecrement = clickDecrement, clickIncrement =
            clickIncrement, clickAdditional = clickAdditional
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