package com.onitsura12.farmdel.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.domain.models.Address
import com.onitsura12.farmdel.databinding.AddressItemBinding


class AddressAdapter(private val markAddress: (address: Address) -> Unit, private val remove:
(address: Address) -> Unit, private val edit: (address: Address) -> Unit) :
    ListAdapter<Address, AddressAdapter.ItemHolder>
    (ItemComparator()) {


    class ItemHolder(
        private val binding: AddressItemBinding,
        private val markAddress: (address: Address) -> Unit, private val remove:
            (address: Address) -> Unit, private val edit: (address: Address) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address) {
            binding.apply {
                tvCity.text = address.city
                tvStreet.text = address.street
                tvHouse.text = address.house
                tvEntrance.text = address.entrance
                tvFloor.text = address.floor
                tvFlat.text = address.flat
                if (address.primary) {
                    tvPrimarySwitch.visibility = View.VISIBLE
                    setPrimaryButton.visibility = View.GONE
                }
                else{
                    tvPrimarySwitch.visibility = View.GONE
                    setPrimaryButton.visibility = View.VISIBLE
                }

                setPrimaryButton.setOnClickListener {
                    markAddress.invoke(address)
                    tvPrimarySwitch.visibility = View.VISIBLE
                    setPrimaryButton.visibility = View.GONE
                }

                editAddressButton.setOnClickListener {
                    edit.invoke(address)
                }

                removeAddressButton.setOnClickListener {
                    remove.invoke(address)
                }
            }
        }


    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            AddressItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false), markAddress = markAddress, remove = remove, edit = edit)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class ItemComparator : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.primary == newItem.primary
        }

    }
}