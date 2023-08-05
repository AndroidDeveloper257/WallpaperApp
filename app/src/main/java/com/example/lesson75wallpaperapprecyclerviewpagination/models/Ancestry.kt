package com.example.lesson75wallpaperapprecyclerviewpagination.models

import com.example.lesson75wallpaperapprecyclerviewpagination.models.Category
import com.example.lesson75wallpaperapprecyclerviewpagination.models.Subcategory
import com.example.lesson75wallpaperapprecyclerviewpagination.models.Type

data class Ancestry(
    val category: Category,
    val subcategory: Subcategory,
    val type: Type
)