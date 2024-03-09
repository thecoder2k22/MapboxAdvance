package com.thecoder.maps.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thecoder.maps.models.MySavedItem

@Dao
interface DbDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addLocation(item:MySavedItem)

    @Query("select * from MySavedItem")
    fun getAllSaved():LiveData<List<MySavedItem>>

}