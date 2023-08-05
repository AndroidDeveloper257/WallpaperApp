package com.example.lesson75wallpaperapprecyclerviewpagination.pagination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class ImageViewModel(var category: String, var isRandom: Boolean) : ViewModel() {
    val flow = Pager(
        PagingConfig(6)
    ) {
        ImageDataPagingDataSource(category, isRandom)
    }.flow
        .cachedIn(viewModelScope)
}