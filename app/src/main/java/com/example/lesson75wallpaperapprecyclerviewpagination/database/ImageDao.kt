package com.example.lesson75wallpaperapprecyclerviewpagination.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert
    fun addImage(image: MyImageEntity)

    @Delete
    fun deleteImage(image: MyImageEntity)

    @Query("SELECT * FROM images_table")
    fun listImages(): List<MyImageEntity>

    @Query("SELECT * FROM images_table WHERE image_url = :imageUrl")
    fun getImageById(imageUrl: String): MyImageEntity
}