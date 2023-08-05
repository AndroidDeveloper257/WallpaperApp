package com.example.lesson75wallpaperapprecyclerviewpagination.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lesson75wallpaperapprecyclerviewpagination.fragments.image_list.ImageListFragment

class ViewPagerAdapter(
    fragment: Fragment,
    private val tabList: ArrayList<String>
) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = tabList.size

    override fun createFragment(position: Int): Fragment {
        return ImageListFragment.newInstance(tabList[position])
    }
}