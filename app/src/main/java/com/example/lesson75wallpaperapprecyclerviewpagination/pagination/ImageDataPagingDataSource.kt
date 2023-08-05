package com.example.lesson75wallpaperapprecyclerviewpagination.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lesson75wallpaperapprecyclerviewpagination.models.Result
import com.example.lesson75wallpaperapprecyclerviewpagination.networking.ApiClient
import com.example.lesson75wallpaperapprecyclerviewpagination.networking.ApiService
import java.lang.Exception
import java.util.*

class ImageDataPagingDataSource(var category: String, var isRandom: Boolean) : PagingSource<Int, Result>() {
    private val apiService = ApiClient.getRetrofit().create(ApiService::class.java)
    private val TAG = "ImageDataPagingDataSour"

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        try {
            var nextPageNumber = params.key ?: 1
            val response = apiService.listPhotos(
                category,
                nextPageNumber,
                10
            )
            if (isRandom) {
                response.body()?.results?.let { Collections.shuffle(it) }
            }
            return if (response.isSuccessful) {
                LoadResult.Page(
                    response.body()?.results ?: emptyList(),
                    null,
                    ++nextPageNumber
                )
            } else {
                LoadResult.Error(Throwable("Error"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "load: ${e.message}")
            return LoadResult.Error(e)
        }
    }
}