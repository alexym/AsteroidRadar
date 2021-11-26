package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.getNextSevenDays
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.constants.Constants
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.asDatabaseModel
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRadarRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDAO.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = Network.arService.getAsteroidsAsync(
                getToday(),
                getNextSevenDays(),
                Constants.KEY_URL
            ).await()
            val parcelAsteroids = parseAsteroidsJsonResult(JSONObject(asteroids))
            database.asteroidDAO.insertAll(parcelAsteroids.asDatabaseModel())
        }
    }
}