package com.example.lesson75wallpaperapprecyclerviewpagination.fragments.random

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.TAB_1
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.TAB_2
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.TAB_3
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.TAB_4
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.TAB_5
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lesson75wallpaperapprecyclerviewpagination.R
import com.example.lesson75wallpaperapprecyclerviewpagination.adapters.ImagePagingAdapter
import com.example.lesson75wallpaperapprecyclerviewpagination.database.MyImageEntity
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.FragmentRandomBinding
import com.example.lesson75wallpaperapprecyclerviewpagination.models.Result
import com.example.lesson75wallpaperapprecyclerviewpagination.pagination.ImageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RandomFragment : Fragment(), CoroutineScope {

    /**
     * Random fragment
     */

    private var _binding: FragmentRandomBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: ImageViewModel
    private lateinit var adapter: ImagePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomBinding.inflate(inflater, container, false)
        viewModel = ImageViewModel("random", true)
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
                }
        }

        return binding.root
    }

    private fun getRandomCategory(): String {
        val list = ArrayList<String>()
        list.add(TAB_1)
        list.add(TAB_2)
        list.add(TAB_3)
        list.add(TAB_4)
        list.add(TAB_5)
        return list[(0 until list.size).random()]
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}