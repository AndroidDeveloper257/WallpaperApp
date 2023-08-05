package com.example.lesson75wallpaperapprecyclerviewpagination.models

data class UnsplashImageData(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
)