package com.example.wallpaperapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wallpaperapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        binding.apply {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setContentView(root)
            setSupportActionBar(appBarMain.toolbar)
            navigationView.itemIconTintList = null
            navController = findNavController(R.id.nav_host_fragment_content_main)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.homeFragment,
                    R.id.popularFragment,
                    R.id.randomFragment,
                    R.id.favoriteFragment
                ), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navigationView.setupWithNavController(navController)
            appBarMain.bottomBar.setOnItemSelectedListener {
                if (navController.currentDestination?.id != it.itemId) {
                    navController.navigate(it.itemId)
                    appBarMain.bottomBar.onNavigationItemSelected(it)
                }
                true
            }
            appBarMain.toolbar.setNavigationOnClickListener {
                if (navController.currentDestination?.id == R.id.historyFragment ||
                    navController.currentDestination?.id == R.id.aboutFragment
                ) {
                    Log.d(TAG, "onCreate: back")
                    navController.popBackStack()
                } else {
                    drawerLayout.open()
                    Log.d(TAG, "onCreate: open drawer")
                }
            }
            navigationView.setNavigationItemSelectedListener {
                navController.navigate(it.itemId)
                when (it.itemId) {
                    R.id.homeFragment, R.id.popularFragment, R.id.randomFragment, R.id.favoriteFragment, R.id.historyFragment, R.id.aboutFragment -> {
                        appBarMain.toolbar.visibility = View.VISIBLE
                        appBarMain.bottomBar.visibility =
                            if (it.itemId != R.id.historyFragment &&
                                it.itemId != R.id.aboutFragment
                            ) {
                                appBarMain.bottomBar.onNavigationItemSelected(
                                    navigationView.menu.findItem(
                                        it.itemId
                                    )
                                )
                                View.VISIBLE
                            } else
                                View.GONE
                    }

                    else -> {
                        appBarMain.toolbar.visibility = View.GONE
                        appBarMain.bottomBar.visibility = View.GONE
                    }
                }
                drawerLayout.close()
                Toast.makeText(this@MainActivity, "${it.title}", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun onNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}