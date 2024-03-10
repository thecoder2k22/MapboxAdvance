package com.thecoder.maps.ui


import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.thecoder.maps.R
import com.thecoder.maps.databinding.ActivitySplashBinding


class ActivitySplash :AppCompatActivity (){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor=getColor(R.color.main_color)
      val  binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 4 seconds splash
        binding.root.postDelayed({
         startActivity(Intent(this,ActivityHome::class.java))
            finish()

        },4000)

    }




}