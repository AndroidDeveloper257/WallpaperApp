package com.example.lesson75wallpaperapprecyclerviewpagination.fragments.open_image

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.lesson75wallpaperapprecyclerviewpagination.R
import com.example.lesson75wallpaperapprecyclerviewpagination.adapters.FilterImageAdapter
import com.example.lesson75wallpaperapprecyclerviewpagination.database.AppDatabase
import com.example.lesson75wallpaperapprecyclerviewpagination.database.MyImageEntity
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.BottomSheetItemBinding
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.FragmentOpenImageBinding
import com.example.lesson75wallpaperapprecyclerviewpagination.filter.PhotoFilter
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.CHOOSE_BACKGROUND
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.FILTERING_APPLIED
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.FILTERING_IN_PROCESS
import com.example.lesson75wallpaperapprecyclerviewpagination.utils.StatusValues.OPEN_IMAGE
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.permissionx.guolindev.PermissionX
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class OpenImageFragment : Fragment() {

    private lateinit var binding: FragmentOpenImageBinding
    private lateinit var currentImage: MyImageEntity
    private lateinit var database: AppDatabase
    private lateinit var likedList: ArrayList<MyImageEntity>
    private var isLiked: Boolean? = null
    private var status = OPEN_IMAGE
    private var originalImageBitmap: Bitmap? = null
    private var isFiltered = false

    private lateinit var filterBitmapList: ArrayList<Bitmap>

    companion object {
        private const val TAG = "OpenImageFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOpenImageBinding.inflate(layoutInflater)
        database = AppDatabase.getInstance(requireContext())
        likedList = try {
            ArrayList(database.imageDao().listImages())
        } catch (e: Exception) {
            Log.e(TAG, "onCreateView: ${e.message}")
            ArrayList()
        }
        val parcelable = arguments?.getParcelable<MyImageEntity>("image")
        if (parcelable != null) {
            currentImage = parcelable
        }
        setData()
        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPress()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callBack)
        /**
         * Back button click progress
         * started
         */
        binding.backBtn.setOnClickListener {
            onBackPress()
        }

        /**
         * Finished
         */

        binding.shareBtn.setOnClickListener {
            shareImage()
        }
        binding.aboutBtn.setOnClickListener {
            if (status == OPEN_IMAGE) {
                showInfo()
            } else {
                if (status == FILTERING_IN_PROCESS) {
                    applyFilter()
                }
            }
        }
        binding.downloadBtn.setOnClickListener {
            downloadImage()
        }
        binding.likeBtn.setOnClickListener {
            if (isLiked == true) {
                database.imageDao().deleteImage(currentImage)
                Toast.makeText(requireContext(), "Removed from liked", Toast.LENGTH_SHORT).show()
                isLiked = false
                binding.likedImg.setImageResource(R.drawable.ic_disliked_icon)
                Log.d(TAG, "onCreateView: Deleted $currentImage")
            } else {
                database.imageDao().addImage(currentImage)
                Toast.makeText(requireContext(), "Added to liked", Toast.LENGTH_SHORT).show()
                isLiked = true
                binding.likedImg.setImageResource(R.drawable.ic_liked_icon)
                Log.d(TAG, "onCreateView: Added $currentImage")
            }
        }
        binding.setBackgroundBtn.setOnClickListener {
            setBackground()
        }
        binding.setFilterBackgroundBtn.setOnClickListener {
            binding.backImg.setImageResource(R.drawable.ic_back_icon)
            binding.bottomFiltered.visibility = View.GONE
            binding.bottomChoice.visibility = View.VISIBLE
            status = CHOOSE_BACKGROUND
        }
        binding.homeScreen.setOnClickListener {
            if (setToScreen(true)) {
                Toast.makeText(
                    requireContext(),
                    "Successfully set to home screen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.lockScreen.setOnClickListener {
            if (setToScreen(false)) {
                Toast.makeText(
                    requireContext(),
                    "Successfully set to lock screen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.homeLockScreen.setOnClickListener {
            if (setToScreen(true) && setToScreen(false)) {
                Toast.makeText(
                    requireContext(),
                    "Successfully set to both screens",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.filterBtn.setOnClickListener {
            filterImage()
        }
        binding.downloadFilterBtn.setOnClickListener {
            downloadImage()
        }
        return binding.root
    }

    private fun onBackPress() {
        if (status == CHOOSE_BACKGROUND) {
            binding.bottomChoice.visibility = View.GONE
            if (isFiltered) {
                binding.bottomFiltered.visibility = View.VISIBLE
                status = FILTERING_APPLIED
                Log.d(TAG, "onBackPress: Filter result")
            } else {
                binding.bottomOpen.visibility = View.VISIBLE
                binding.shareBtn.visibility = View.VISIBLE
                binding.aboutImg.setImageResource(R.drawable.ic_about_icon)
                binding.backImg.setImageResource(R.drawable.ic_back_icon)
                binding.aboutBtn.visibility = View.VISIBLE
                status = OPEN_IMAGE
                Log.d(TAG, "onBackPress: Photo review from simple action")
            }
        } else {
            if (status == FILTERING_APPLIED) {
                binding.bottomFiltered.visibility = View.GONE
                binding.rv.visibility = View.VISIBLE
                binding.backImg.setImageResource(R.drawable.ic_close_icon)
                binding.aboutImg.setImageResource(R.drawable.ic_apply_icon)
                binding.aboutBtn.visibility = View.VISIBLE
                status = FILTERING_IN_PROCESS
                Log.d(TAG, "onBackPress: Filter")
            } else {
                if (status == FILTERING_IN_PROCESS) {
                    isFiltered = false
                    binding.rv.visibility = View.GONE
                    binding.bottomOpen.visibility = View.VISIBLE
                    binding.image.setImageBitmap(originalImageBitmap)
                    binding.backImg.setImageResource(R.drawable.ic_back_icon)
                    binding.shareBtn.visibility = View.VISIBLE
                    binding.aboutImg.setImageResource(R.drawable.ic_about_icon)
                    binding.bottomOpen.visibility = View.VISIBLE
                    status = OPEN_IMAGE
                    Log.d(TAG, "onBackPress: Photo review from filter action")
                } else {
                    if (status == OPEN_IMAGE) {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun applyFilter() {
        binding.aboutBtn.visibility = View.GONE
        binding.backImg.setImageResource(R.drawable.ic_back_icon)
        binding.rv.visibility = View.GONE
        binding.bottomFiltered.visibility = View.VISIBLE
        status = FILTERING_APPLIED
        isFiltered = true
    }

    @SuppressLint("ResourceType")
    private fun filterImage() {
        binding.backImg.setImageResource(R.drawable.ic_close_icon)
        binding.shareBtn.visibility = View.GONE
        binding.aboutImg.setImageResource(R.drawable.ic_apply_icon)
        binding.bottomOpen.visibility = View.GONE
        filterBitmapList = ArrayList()
        val filter = PhotoFilter()
        val bitmapDrawable = binding.image.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        filterBitmapList.add(filter.one(requireContext(), bitmap))
        filterBitmapList.add(filter.two(requireContext(), bitmap))
        filterBitmapList.add(filter.three(requireContext(), bitmap))
        filterBitmapList.add(filter.four(requireContext(), bitmap))
        filterBitmapList.add(filter.five(requireContext(), bitmap))
        filterBitmapList.add(filter.six(requireContext(), bitmap))
        filterBitmapList.add(filter.seven(requireContext(), bitmap))
        filterBitmapList.add(filter.eight(requireContext(), bitmap))
        filterBitmapList.add(filter.nine(requireContext(), bitmap))
        filterBitmapList.add(filter.ten(requireContext(), bitmap))
        filterBitmapList.add(filter.eleven(requireContext(), bitmap))
        filterBitmapList.add(filter.twelve(requireContext(), bitmap))
        filterBitmapList.add(filter.thirteen(requireContext(), bitmap))
        filterBitmapList.add(filter.fourteen(requireContext(), bitmap))
        filterBitmapList.add(filter.fifteen(requireContext(), bitmap))
        filterBitmapList.add(filter.sixteen(requireContext(), bitmap))
        val adapter = FilterImageAdapter(
            filterBitmapList
        ) {
            binding.image.setImageBitmap(it)
            isFiltered = true
        }
        binding.rv.visibility = View.VISIBLE
        binding.rv.adapter = adapter
        status = FILTERING_IN_PROCESS
    }

    private fun setToScreen(isHome: Boolean): Boolean {
        var result = false
        val bitmapDrawable = binding.image.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val wallpaperManager = WallpaperManager.getInstance(requireContext())
        try {
            if (isHome) {
                wallpaperManager.setBitmap(bitmap)
                result = true
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                result = true
            } else Toast.makeText(
                requireContext(),
                "Couldn't set to lock screen",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Log.e(TAG, "setToScreen: ${e.message}")
        }
        return result
    }

    private fun setBackground() {
        binding.shareBtn.visibility = View.GONE
        binding.aboutBtn.visibility = View.GONE
        binding.bottomOpen.visibility = View.GONE
        binding.bottomChoice.visibility = View.VISIBLE
        binding.backImg.setImageResource(R.drawable.ic_close_icon)
        status = CHOOSE_BACKGROUND
    }

    private fun downloadImage() {
        PermissionX.init(activity)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Core fundamental are based on these permissions",
                    "OK",
                    "Cancel"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "You need to allow necessary permissions in Settings manually",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    saveToStorage()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun saveToStorage() {
        var contentResolver = requireActivity().contentResolver
        val images = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "${currentImage.author}_${getTime()}.jpg"
        )
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*")
        val uri = contentResolver.insert(images, contentValues)

        try {
            val bitmapDrawable = binding.image.drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap

            val outputStream = Objects.requireNonNull(uri)
                ?.let { contentResolver.openOutputStream(it) }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            Objects.requireNonNull(outputStream)
            Toast.makeText(requireContext(), "Saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTime(): String {
        var result = "YYYY_MM_DD_HH_mm_ss"
        val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val year = now.year
        val month = now.month
        val dayOfMonth = now.dayOfMonth
        val hour = now.hour
        val minute = now.minute
        val second = now.second
        result = "${year}-${month}-${dayOfMonth}-${hour}-${minute}-${second}"
        return result
    }

    private fun showInfo() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetItemBinding = BottomSheetItemBinding.inflate(layoutInflater)
        bottomSheetItemBinding.authorTv.text = "Author: ${currentImage.author}"
        bottomSheetItemBinding.sizeTv.text = "Size: ${currentImage.width}x${currentImage.height}"
        bottomSheetDialog.setContentView(bottomSheetItemBinding.root)
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.show()

        binding.shareBtn.visibility = View.GONE
        binding.aboutBtn.visibility = View.GONE

        bottomSheetDialog.setOnCancelListener {
            binding.shareBtn.visibility = View.VISIBLE
            binding.aboutBtn.visibility = View.VISIBLE
        }
    }

    private fun shareImage() {
        val bitmapDrawable = binding.image.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        val bmpUri: Uri = saveImage(bitmap)
        val textToShare = "Alimov Jasur's wallpaper app"
        share.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        share.putExtra(Intent.EXTRA_STREAM, bmpUri)
        share.putExtra(Intent.EXTRA_SUBJECT, "New app")
        share.putExtra(Intent.EXTRA_TEXT, textToShare)
        startActivity(Intent.createChooser(share, "Share wallpaper"))
    }

    private fun saveImage(bitmap: Bitmap?): Uri {
        val imagesFolder = File(context?.cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "wallpaper.jpg")
            val stream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            stream.flush()
            stream.close()
            uri = Objects.requireNonNull(
                context?.applicationContext
            )?.let {
                FileProvider.getUriForFile(
                    it,
                    "com.example.lesson75wallpaperapprecyclerviewpagination" + ".provider",
                    file
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "saveImage: ${e.message}")
        }
        return uri!!
    }

    private fun setData() {
        isLiked = checkImage()
        loadImage()
    }

    private fun loadImage() {
        Picasso
            .get()
            .load(currentImage.imgUrl)
            .into(binding.image, object : Callback {
                override fun onSuccess() {
                    binding.progressCircular.visibility = View.GONE
                    if (isLiked == true) binding.likedImg.setImageResource(R.drawable.ic_liked_icon)
                    else binding.likedImg.setImageResource(R.drawable.ic_disliked_icon)
                    originalImageBitmap = (binding.image.drawable as BitmapDrawable).bitmap
                }

                override fun onError(e: Exception?) {

                }

            })
    }

    private fun checkImage(): Boolean? {
        likedList.forEach {
            if (it.imgUrl == currentImage.imgUrl) return true
        }
        return false
    }
}