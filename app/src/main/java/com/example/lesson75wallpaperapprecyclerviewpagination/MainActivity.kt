package com.example.lesson75wallpaperapprecyclerviewpagination

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lesson75wallpaperapprecyclerviewpagination.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navView.itemIconTintList = null
        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.popularFragment,
                R.id.randomFragment,
                R.id.likedFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.appBarMain.toolbar.visibility = View.VISIBLE
                    binding.appBarMain.cardView.visibility = View.VISIBLE
                    binding.appBarMain.homeCircle.visibility = View.VISIBLE
                    binding.appBarMain.favCircle.visibility = View.GONE
                    binding.appBarMain.randomCircle.visibility = View.GONE
                    binding.appBarMain.likeCircle.visibility = View.GONE
                }
                R.id.popularFragment -> {
                    binding.appBarMain.toolbar.visibility = View.VISIBLE
                    binding.appBarMain.cardView.visibility = View.VISIBLE
                    binding.appBarMain.homeCircle.visibility = View.GONE
                    binding.appBarMain.favCircle.visibility = View.VISIBLE
                    binding.appBarMain.randomCircle.visibility = View.GONE
                    binding.appBarMain.likeCircle.visibility = View.GONE
                }
                R.id.randomFragment -> {
                    binding.appBarMain.toolbar.visibility = View.VISIBLE
                    binding.appBarMain.cardView.visibility = View.VISIBLE
                    binding.appBarMain.homeCircle.visibility = View.GONE
                    binding.appBarMain.favCircle.visibility = View.GONE
                    binding.appBarMain.randomCircle.visibility = View.VISIBLE
                    binding.appBarMain.likeCircle.visibility = View.GONE
                }
                R.id.likedFragment -> {
                    binding.appBarMain.toolbar.visibility = View.VISIBLE
                    binding.appBarMain.cardView.visibility = View.VISIBLE
                    binding.appBarMain.homeCircle.visibility = View.GONE
                    binding.appBarMain.favCircle.visibility = View.GONE
                    binding.appBarMain.randomCircle.visibility = View.GONE
                    binding.appBarMain.likeCircle.visibility = View.VISIBLE
                }
                R.id.historyFragment -> {
                    binding.appBarMain.toolbar.visibility = View.VISIBLE
                    binding.appBarMain.cardView.visibility = View.GONE
                }
                R.id.aboutFragment -> {
                    binding.appBarMain.toolbar.visibility = View.VISIBLE
                    binding.appBarMain.cardView.visibility = View.GONE
                }
                else -> {
                    binding.appBarMain.toolbar.visibility = View.GONE
                    binding.appBarMain.cardView.visibility = View.GONE
                }
            }
        }

        binding.appBarMain.homeImg.setOnClickListener {
            if (navController.currentDestination?.id != R.id.homeFragment) {
                navController.navigate(R.id.homeFragment)
            }
        }
        binding.appBarMain.favImg.setOnClickListener {
            if (navController.currentDestination?.id != R.id.popularFragment) {
                navController.navigate(R.id.popularFragment)
            }
        }
        binding.appBarMain.randomImg.setOnClickListener {
            if (navController.currentDestination?.id != R.id.randomFragment) {
                navController.navigate(R.id.randomFragment)
            }
        }
        binding.appBarMain.likeImg.setOnClickListener {
            if (navController.currentDestination?.id != R.id.likedFragment) {
                navController.navigate(R.id.likedFragment)
            }
        }

        binding.pexels.setOnClickListener { view -> open("https://www.pexels.com/") }
        binding.unsplash.setOnClickListener { view -> open("https://unsplash.com//") }
    }

    private fun open(url: String) {
        val bundle = Bundle()
        bundle.putString("url", url)
        binding.drawerLayout.closeDrawers()
        navController.navigate(R.id.webFragment, bundle)
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}