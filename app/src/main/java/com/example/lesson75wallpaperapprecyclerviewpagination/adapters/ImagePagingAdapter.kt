package com.example.lesson75wallpaperapprecyclerviewpagination.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.ImageItemBinding
import com.example.lesson75wallpaperapprecyclerviewpagination.models.Result
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ImagePagingAdapter(
    var context: Context,
    var onImageClick:(Result) -> Unit
) : PagingDataAdapter<Result, ImagePagingAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

    }

    inner class Vh(var itemBinding: ImageItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(result: Result?) {
            val url = result?.cover_photo?.urls?.regular
            Picasso
                .get()
                .load(url)
                .into(itemBinding.image, object : Callback {
                    override fun onSuccess() {
                        itemBinding.progressCircular.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {

                    }

                })
            itemBinding.image.setOnClickListener {
                if (result != null) {
                    onImageClick.invoke(result)
                } else {
                    Toast.makeText(
                        context,
                        "Oops something went wrong with getting image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}