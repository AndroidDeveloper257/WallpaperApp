package com.example.wallpaperapp

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.wallpaperapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setContentView(root)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                appBarMain.bottomCard.setRenderEffect(
                    RenderEffect.createBlurEffect(
                        10F,
                        10F,
                        Shader.TileMode.MIRROR
                    )
                )
            }
        }
    }
}