package com.critx.shwemiAdmin

import  com.critx.shwemiAdmin.R
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.critx.shwemiAdmin.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var actionBarDrawerToggle : ActionBarDrawerToggle
    private val startDestinationList = listOf<Int>(
        R.id.dailyGoldPriceFragment,
        R.id.sampleTakeAndReturnFragment,
        R.id.confirmVoucherFragment,
        R.id.pointFragment,
        R.id.discountFragment,
        R.id.flashSaleFragment,
        R.id.collectStockFragment,
        R.id.setupStockFragment
    )

    private val imgUrl =
        "https://live-production.wcms.abc-cdn.net.au/ff1221fbfdb2fe163fdda15df5f77676?impolicy=wcms_crop_resize&cropH=394&cropW=700&xPos=0&yPos=37&width=862&height=485"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<NavigationView>(R.id.navigation)

        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close)
        actionBarDrawerToggle.syncState()
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)

        val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setSupportActionBar(binding.materialToolbar)
        binding.navigation.setupWithNavController(navController)
//        binding.materialToolbar.setupWithNavController(navController,appBarConfiguration)
//        NavigationUI.setupWithNavController(binding.materialToolbar, navController, binding.drawerLayout)


        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _ ->
//            binding.drawerLayout.setDrawerLockMode(
//                if (nd.id == nc.graph.startDestinationId) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
//            )
            binding.materialToolbar.visibility =
                if (startDestinationList.contains(nd.id)) View.VISIBLE else View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.dailyGoldPriceFragment->{

            }
            R.id.sampleTakeAndReturnFragment->{

            }

        }
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}