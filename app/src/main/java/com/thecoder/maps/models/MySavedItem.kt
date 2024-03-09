package com.thecoder.maps.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// data class created for saving location
// selected by user
@Entity
data class MySavedItem(val address:String,val lat:Double,val longi:Double,@PrimaryKey(autoGenerate = true)var key:Long=0):Serializable
