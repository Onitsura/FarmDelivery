package com.onitsura12.farmdel.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.onitsura12.farmdel.R
import com.onitsura12.farmdel.databinding.SlideItemContainerBinding
import com.squareup.picasso.Picasso

class SliderAdapter: RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private val listImages: ArrayList<String> = arrayListOf()
    private lateinit var viewPager2: ViewPager2

    fun setData(newList: ArrayList<String>, _viewPager2: ViewPager2){
        listImages.clear()
        listImages.addAll(newList)
        viewPager2 = _viewPager2
    }

    class SliderViewHolder(private val binding: SlideItemContainerBinding) : RecyclerView
    .ViewHolder(binding.root) {

        fun bind(imagePath: String){
            if (imagePath.isNotEmpty() && imagePath.isNotBlank()) {
                Picasso.get()
                    .load(imagePath)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .fit()
                    .into(binding.imageSlider)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            SlideItemContainerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(listImages[position])
    }

    override fun getItemCount(): Int {
        return listImages.size
    }
}