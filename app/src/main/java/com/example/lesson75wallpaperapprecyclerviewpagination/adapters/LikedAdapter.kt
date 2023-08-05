package com.example.lesson75wallpaperapprecyclerviewpagination.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson75wallpaperapprecyclerviewpagination.database.MyImageEntity
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.ImageItemBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class LikedAdapter(
    var likedList: ArrayList<MyImageEntity>,
    var context: Context,
    var onImageClick: (MyImageEntity) -> Unit
) : RecyclerView.Adapter<LikedAdapter.Vh>() {

    inner class Vh(var itemBinding: ImageItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(image: MyImageEntity?) {
            val url = image?.imgUrl
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
                if (image != null) {
                    onImageClick.invoke(image)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(likedList[position])
    }

    override fun getItemCount(): Int = likedList.size
}