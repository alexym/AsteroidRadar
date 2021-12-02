package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidRadarDAO {
    @Query("select * from databaseasteroid WHERE closeApproachDate >= :date ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(date:String): List<DatabaseAsteroid>

    @Query("select * from databaseasteroid WHERE closeApproachDate = :date")
    fun getTodayAsteroids(date:String): List<DatabaseAsteroid>

    @Query("select * from databaseasteroid")
    fun getALLAsteroids(): List<DatabaseAsteroid>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<DatabaseAsteroid>)

    @Query("DELETE FROM databaseasteroid WHERE closeApproachDate < :date")
    fun clear(date:String):Int
}