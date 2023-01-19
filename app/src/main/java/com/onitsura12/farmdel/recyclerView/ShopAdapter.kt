package com.onitsura12.farmdel.recyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.ShopItemBinding
import com.squareup.picasso.Picasso

class ShopAdapter : ListAdapter<ShopItem, ShopAdapter.ItemHolder>(ItemComparator()) {

    private var mListener: OnListItemClickListener? = null


    fun setListener(listener: OnListItemClickListener) {
        mListener = listener
    }

    class ItemHolder(private val binding: ShopItemBinding, listener: OnListItemClickListener?) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val mListener = listener

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
                itemView.setOnClickListener(this@ItemHolder)

            }
        }
        override fun onClick(view: View?) {
            if (view != null) {
                mListener?.onClick(view = view as ViewGroup, position = layoutPosition)
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            ShopItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), listener = mListener
        )
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
//    override fun getItemId(position: Int): Long {
//        return getItem(position).cost.toLong()
//    }
}
