package com.example.lesson75wallpaperapprecyclerviewpagination.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images_table")
data class MyImageEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "image_url")
    var imgUrl: String,
    @ColumnInfo(name = "author")
    var author: String?,
    @ColumnInfo(name = "width")
    var width: Int,
    @ColumnInfo(name = "height")
    var height: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imgUrl)
        parcel.writeString(author)
        parcel.writeInt(width)
        parcel.writeInt(height)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyImageEntity> {
        override fun createFromParcel(parcel: Parcel): MyImageEntity {
            return MyImageEntity(parcel)
        }

        override fun newArray(size: Int): Array<MyImageEntity?> {
            return arrayOfNulls(size)
        }
    }
}