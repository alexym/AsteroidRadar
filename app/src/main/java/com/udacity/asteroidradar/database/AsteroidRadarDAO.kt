package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidRadarDAO {
    @Query("select * from databaseasteroid WHERE closeApproachDate >= :date ORDER BY closeApproachDate ASC")
    fun getAsteroids(date:String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<DatabaseAsteroid>)
}