package com.example.lesson75wallpaperapprecyclerviewpagination.networking

import com.example.lesson75wallpaperapprecyclerviewpagination.models.UnsplashImageData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/collections")
    suspend fun listPhotos(
        @Query(value = "query") category: String,
        @Query(value = "page") page: Int,
        @Query(value = "per_page") perPage: Int,
        @Query(value = "client_id") clientId: String = "qiA7z7IVKODhn4prqLO0U7mwHxbXU6tKiT-5iqAIB-o"
    ): Response<UnsplashImageData>

}