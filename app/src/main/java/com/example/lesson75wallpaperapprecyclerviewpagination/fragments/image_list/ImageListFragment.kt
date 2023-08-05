package com.example.lesson75wallpaperapprecyclerviewpagination.fragments.image_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson75wallpaperapprecyclerviewpagination.R
import com.example.lesson75wallpaperapprecyclerviewpagination.adapters.ImagePagingAdapter
import com.example.lesson75wallpaperapprecyclerviewpagination.database.MyImageEntity
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.FragmentImageListBinding
import com.example.lesson75wallpaperapprecyclerviewpagination.models.Result
import com.example.lesson75wallpaperapprecyclerviewpagination.pagination.ImageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ImageListFragment : Fragment(), CoroutineScope {

    private lateinit var binding: FragmentImageListBinding
    private lateinit var category: String
    private lateinit var viewModel: ImageViewModel
    private lateinit var adapter: ImagePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageListBinding.inflate(layoutInflater)
        val s = arguments?.getString("title")
        if (s != null) {
            category = s
        }

        viewModel = ImageViewModel(category, false)

        adapter = ImagePagingAdapter(
            requireContext()
        ) { result ->
            openImage(result)
        }
        binding.rv.adapter = adapter
        launch {
            viewModel
                .flow
                .collect {
                    adapter.submitData(it)
                    Toast.makeText(requireContext(), category, Toast.LENGTH_SHORT).show()
                }
        }

//        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (dy > 0) {
//                    Toast.makeText(requireContext(), "$dy > 0", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(requireContext(), "$dy < 0", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

        return binding.root
    }

    private fun openImage(result: Result) {
        val bundle = Bundle()
        val image = MyImageEntity(
            result.cover_photo.urls.regular,
            result.user.name,
            result.cover_photo.width,
            result.cover_photo.height
        )
        bundle.putParcelable("image", image)
        findNavController().navigate(R.id.openImageFragment, bundle)
    }

    companion object {
        private const val TAG = "ImageListFragment"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(title: String) =
            ImageListFragment().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                }
            }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}