package com.thecoder.maps.models

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thecoder.maps.database.Romeo


class SavedViewModel(app:Application):AndroidViewModel (app){
// viewmodel for handling data
    val db =Romeo.getDatabase(app)
    val locations =   db.dao().getAllSaved()

    fun getlocations():LiveData<List<MySavedItem>>{
        return locations
    }

    fun addLocation(item: MySavedItem){

      db.dao().addLocation(item)
    }

}