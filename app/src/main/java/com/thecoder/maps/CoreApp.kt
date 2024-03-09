package com.thecoder.maps

import android.app.Application
import com.mapbox.common.MapboxOptions

class CoreApp:Application() {

    override fun onCreate() {
        super.onCreate()

        MapboxOptions.accessToken="pk.eyJ1IjoiZmFoZWVtc2FoYWIiLCJhIjoiY2xjaDlueHkzNXd4NzNucGptbW9mbWl2YiJ9.swIC19-6YNrOjmLnJvVT1w"
    }
}