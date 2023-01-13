package com.onitsura12.farmdel.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.farmdel.databinding.AddressItemBinding
import com.onitsura12.domain.models.Address


class AddressAdapter : ListAdapter<Address, AddressAdapter.ItemHolder>(ItemComparator()) {



    class ItemHolder(private val binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address){
            binding.apply {
                tvCity.text = address.city
                tvStreet.text = address.street
                tvEntrance.text = address.street
                tvFloor.text = address.floor
                tvFlat.text = address.flat
            }
        }

        companion object{
            fun create(parent: ViewGroup): ItemHolder{
                return ItemHolder(AddressItemBinding.inflate(LayoutInflater.from(parent.context),
                    parent, false))
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent = parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }




    class ItemComparator() : DiffUtil.ItemCallback<Address>(){
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

    }
}