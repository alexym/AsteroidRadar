package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.getImgOfTheDayObj
import com.udacity.asteroidradar.api.getNextSevenDays
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.AsteroidImg
import com.udacity.asteroidradar.domain.asDatabaseModel
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRadarRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDAO.getAsteroids()) {
            it.asDomainModel()
        }

    private val _imgOfTheDay = MutableLiveData<AsteroidImg>()

    val imgOfTheDay: LiveData<AsteroidImg>
        get() = _imgOfTheDay

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = Network.arService.getAsteroidsAsync(
                getToday(),
                getNextSevenDays(),
                BuildConfig.API_KEY
            ).await()
            val parcelAsteroids = parseAsteroidsJsonResult(JSONObject(asteroids))
            database.asteroidDAO.insertAll(parcelAsteroids.asDatabaseModel())
        }
    }


    suspend fun getImageOfTheDay():AsteroidImg {
        val parcelAsteroids:AsteroidImg
        withContext(Dispatchers.IO) {
            val asteroidsImg = Network.arService.getAsteroidsImgAsync(
                BuildConfig.API_KEY
            ).await()
            Timber.i(asteroidsImg)
             parcelAsteroids = getImgOfTheDayObj(JSONObject(asteroidsImg))
        }
        return parcelAsteroids
    }
}