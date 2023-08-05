package com.example.lesson75wallpaperapprecyclerviewpagination.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.FilterItemBinding

class FilterImageAdapter(
    var filterBitmapList: ArrayList<Bitmap>,
    var onItemClick:(Bitmap) -> Unit
) : RecyclerView.Adapter<FilterImageAdapter.Vh>(){

    inner class Vh(var itemBinding: FilterItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(bitmap: Bitmap) {
            itemBinding.image.setImageBitmap(bitmap)
            itemBinding.root.setOnClickListener { onItemClick.invoke(bitmap) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(FilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(filterBitmapList[position])
    }

    override fun getItemCount(): Int = filterBitmapList.size
}