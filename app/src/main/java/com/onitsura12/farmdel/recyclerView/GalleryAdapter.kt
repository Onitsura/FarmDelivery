package com.onitsura12.farmdel.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.BottomSheetItemBinding
import com.onitsura12.farmdel.models.ImageModel
import com.squareup.picasso.Picasso

class GalleryAdapter : ListAdapter<ImageModel, GalleryAdapter.ItemHolder>(ItemComparator()) {

    private var mListener: OnListItemClickListener? = null


    fun setListener(listener: OnListItemClickListener) {
        mListener = listener
    }

    class ItemHolder(
        private val binding: BottomSheetItemBinding, listener: OnListItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val mListener = listener
        fun bind(image: ImageModel) {
            binding.apply {
                Picasso.get().load(image.path)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .fit()
                    .into(imageView)

            }
            itemView.setOnClickListener(this@ItemHolder)
        }

        override fun onClick(view: View?) {
            mListener?.onClick(view = view!! as ViewGroup, position = adapterPosition)
        }


    }


    class ItemComparator() : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            BottomSheetItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener = mListener
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }
}