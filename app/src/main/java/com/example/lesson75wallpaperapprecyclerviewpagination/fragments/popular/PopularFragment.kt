package com.example.lesson75wallpaperapprecyclerviewpagination.fragments.popular

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lesson75wallpaperapprecyclerviewpagination.R
import com.example.lesson75wallpaperapprecyclerviewpagination.adapters.ImagePagingAdapter
import com.example.lesson75wallpaperapprecyclerviewpagination.adapters.ViewPagerAdapter
import com.example.lesson75wallpaperapprecyclerviewpagination.database.MyImageEntity
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.FragmentPopularBinding
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.TabItemBinding
import com.example.lesson75wallpaperapprecyclerviewpagination.models.Result
import com.example.lesson75wallpaperapprecyclerviewpagination.pagination.ImageViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PopularFragment : Fragment(), CoroutineScope {

    /**
     * Popular fragment
     */

    private var _binding: FragmentPopularBinding? = null

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
        _binding = FragmentPopularBinding.inflate(inflater, container, false)

        viewModel = ImageViewModel("popular", false)

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