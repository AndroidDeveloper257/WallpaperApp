package com.example.lesson75wallpaperapprecyclerviewpagination.fragments.liked

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.lesson75wallpaperapprecyclerviewpagination.R
import com.example.lesson75wallpaperapprecyclerviewpagination.adapters.LikedAdapter
import com.example.lesson75wallpaperapprecyclerviewpagination.database.AppDatabase
import com.example.lesson75wallpaperapprecyclerviewpagination.database.ImageEntity
import com.example.lesson75wallpaperapprecyclerviewpagination.database.MyImageEntity
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.FragmentLikedBinding
import com.example.lesson75wallpaperapprecyclerviewpagination.models.Result

class LikedFragment : Fragment() {

    companion object {
        private const val TAG = "LikedFragment"
    }

    private lateinit var binding: FragmentLikedBinding
    private lateinit var database: AppDatabase
    private lateinit var likedList: ArrayList<MyImageEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikedBinding.inflate(layoutInflater)
        database = AppDatabase.getInstance(requireContext())
        likedList = ArrayList(database.imageDao().listImages())
        checkEmpty()
        val adapter = LikedAdapter(
            likedList,
            requireContext()
        ) { imageEntity ->
            openImage(imageEntity)
        }
        binding.rv.adapter = adapter
        return binding.root
    }

    private fun openImage(image: MyImageEntity) {
        val bundle = Bundle()
        bundle.putParcelable("image", image)
        findNavController().navigate(R.id.openImageFragment, bundle)
    }

    private fun checkEmpty() {
        if (likedList.isEmpty()) binding.emptyTv.visibility = View.VISIBLE
        else binding.emptyTv.visibility = View.GONE
    }
}