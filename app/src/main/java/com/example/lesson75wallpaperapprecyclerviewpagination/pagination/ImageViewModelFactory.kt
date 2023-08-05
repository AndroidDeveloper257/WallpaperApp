package com.example.lesson75wallpaperapprecyclerviewpagination.pagination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImageViewModelFactory(var category: String, var isRandom: Boolean) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModel::class.java)) {
            return ImageViewModel(category, isRandom) as T
        }
        return super.create(modelClass)
    }
}