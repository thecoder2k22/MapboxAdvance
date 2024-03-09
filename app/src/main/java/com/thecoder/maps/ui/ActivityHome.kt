package com.thecoder.maps.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation

import androidx.navigation.ui.setupWithNavController

import com.thecoder.maps.R
import com.thecoder.maps.databinding.ActivityHomeBinding
import com.thecoder.maps.utils.Util

class ActivityHome :AppCompatActivity (){


    lateinit var navController: NavController
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
      navController=Navigation.findNavController(findViewById(R.id.nav_host))

binding.bottomNavigation.setupWithNavController(navController)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(0){
                handleBackpress()
            }
        }
    }



    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
      handleBackpress()

    }

    fun handleBackpress(){
        if(navController.currentDestination?.id==R.id.map_frag){
            Util.showDialog(this, getString(R.string.exit_app), getString(R.string.exit_msg),{
                finishAffinity()

                // running GC
                System.gc()

            }, getString(R.string.yes), getString(R.string.no))
        }
    }

}