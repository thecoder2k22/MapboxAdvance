package com.thecoder.maps.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thecoder.maps.models.MySavedItem

@Database(entities = [MySavedItem::class], version = 1)
abstract class Romeo :RoomDatabase(){

    abstract fun dao():DbDao

    companion object{
         var romeo: Romeo?=null

        fun getDatabase(context: Context): Romeo {
            return romeo ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Romeo::class.java,
                    "my_db"
                ).allowMainThreadQueries().build()
                romeo = instance
                instance
            }
        }

    }

}