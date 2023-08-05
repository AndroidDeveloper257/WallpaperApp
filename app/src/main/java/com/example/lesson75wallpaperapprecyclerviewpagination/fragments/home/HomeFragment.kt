package com.example.lesson75wallpaperapprecyclerviewpagination.fragments.home

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
import com.example.lesson75wallpaperapprecyclerviewpagination.R
import com.example.lesson75wallpaperapprecyclerviewpagination.adapters.ViewPagerAdapter
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.FragmentHomeBinding
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.TabItemBinding
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.TAB_1
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.TAB_2
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.TAB_3
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.TAB_4
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.TAB_5
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var tabList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        loadTabs()

        val adapter = ViewPagerAdapter(this, tabList)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position -> }
            .attach()

        setTabs()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    val tabItemBinding = tab.customView?.let { TabItemBinding.bind(it) }
                    tabItemBinding?.title?.setTextColor(Color.WHITE)
                    tabItemBinding?.circle?.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    val tabItemBinding = tab.customView?.let { TabItemBinding.bind(it) }
                    tabItemBinding?.title?.setTextColor(Color.parseColor("#808B93"))
                    tabItemBinding?.circle?.visibility = View.GONE
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    private fun setTabs() {
        val tabCount = binding.tabLayout.tabCount
        for (i in 0 until tabCount) {
            val tab = binding.tabLayout.getTabAt(i)
            val tabItemBinding = TabItemBinding.inflate(LayoutInflater.from(requireContext()))
            tabItemBinding.title.text = tabList[i]
            if (i == 0) {
                tabItemBinding.title.setTextColor(Color.WHITE)
                tabItemBinding.circle.visibility = View.VISIBLE
            } else {
                tabItemBinding.title.setTextColor(Color.parseColor("#808B93"))
                tabItemBinding.circle.visibility = View.GONE
            }
            tab?.customView = tabItemBinding.root
        }
    }

    private fun loadTabs() {
        tabList = ArrayList()
        tabList.add(TAB_1)
        tabList.add(TAB_2)
        tabList.add(TAB_3)
        tabList.add(TAB_4)
        tabList.add(TAB_5)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*override fun onResume() {
        super.onResume()
        val circle = requireActivity().findViewById<LinearLayout>(R.id.home_circle)
        circle.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        val circle = requireActivity().findViewById<LinearLayout>(R.id.home_circle)
        circle.visibility = View.GONE
    }*/
}