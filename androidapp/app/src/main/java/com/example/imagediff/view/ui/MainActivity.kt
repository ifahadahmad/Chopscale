package com.example.imagediff.view.ui

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.imagediff.R
import com.example.imagediff.databinding.ActivityMainBinding
import com.example.imagediff.service.model.ImageModel
import com.example.imagediff.view.adapter.CustomAdapter
import com.example.imagediff.viewmodel.ImageViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class MainActivity() : AppCompatActivity() {
    private val TAG:String = "ACTIVITY"
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageViewModel: ImageViewModel
    private var imageList: ArrayList<ImageModel> = ArrayList()
    private lateinit var adapter: CustomAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        imageViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        permissions()
        val navHostFragment:NavHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView3) as NavHostFragment
        val navController:NavController = navHostFragment.navController
        val bottomNavigationView:BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val appBarLayout = findViewById<AppBarLayout>(R.id.appBarLayout)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.landingFragment || destination.id == R.id.recentFragment ||destination.id==R.id.navigation_dialog_fragment) {

                bottomNavigationView.visibility = View.VISIBLE
                appBarLayout.visibility = View.VISIBLE
            } else {

                bottomNavigationView.visibility = View.GONE
                appBarLayout.visibility = View.GONE
            }
        }
        val set= HashSet<Int>()
        set.add(R.id.landingFragment)
        set.add(R.id.recentFragment)
        val appBarConfiguration = AppBarConfiguration.Builder(set).build()
        val appBar:androidx.appcompat.widget.Toolbar = findViewById(R.id.topAppBar)
        NavigationUI.setupWithNavController(appBar,navController,appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

    }

    fun permissions(){


        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                imageViewModel.getAllImages()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                findNavController(R.id.fragmentContainerView3).navigateUp()
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
            .setDeniedMessage("You need Storage permissions to show images!!")
            .check()


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            super.onCreateOptionsMenu(menu)
            menuInflater.inflate(R.menu.menu_main,menu)
            return true
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
}

