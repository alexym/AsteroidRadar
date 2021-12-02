package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import java.io.IOException

enum class AsteroidFilter(val value: String) { SHOW_TODAY("today"), SHOW_WEEK("week"), SHOW_ALL("all") }
class AsteroidRadarRepository(private val database: AsteroidDatabase) {

    private val _imgOfTheDay = MutableLiveData<AsteroidImg>()
    private val _asteroidList = MutableLiveData<List<Asteroid>>()

    val imgOfTheDay: LiveData<AsteroidImg>
        get() = _imgOfTheDay
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    suspend fun getAsteroids(filter: AsteroidFilter) {
        withContext(Dispatchers.IO) {
            when (filter) {
                AsteroidFilter.SHOW_WEEK -> _asteroidList.postValue(
                    database.asteroidDAO.getWeekAsteroids(
                        getToday()
                    ).asDomainModel()
                )

                AsteroidFilter.SHOW_TODAY -> _asteroidList.postValue(
                    database.asteroidDAO.getTodayAsteroids(
                        getToday()
                    ).asDomainModel()
                )

                AsteroidFilter.SHOW_ALL -> _asteroidList.postValue(
                    database.asteroidDAO.getALLAsteroids().asDomainModel()
                )
            }
        }
    }


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids = Network.arService.getAsteroidsAsync(
                    getToday(),
                    getNextSevenDays(),
                    BuildConfig.API_KEY
                ).await()
                val parcelAsteroids = parseAsteroidsJsonResult(JSONObject(asteroids))
                database.asteroidDAO.insertAll(parcelAsteroids.asDatabaseModel())
            } catch (networkError: IOException) {
                networkError.printStackTrace()
            }
        }
    }


    suspend fun getImageOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsImg = Network.arService.getAsteroidsImgAsync(
                    BuildConfig.API_KEY
                ).await()
                val imgOfTheDayObj = getImgOfTheDayObj(JSONObject(asteroidsImg))
                if (imgOfTheDayObj.media_type == "image")
                    _imgOfTheDay.postValue(imgOfTheDayObj)
            } catch (networkError: IOException) {
                networkError.printStackTrace()
            }
        }
    }

    suspend fun clearPreviousAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val numRows = database.asteroidDAO.clear(getToday())
                Timber.i("esto se borro '%s'", numRows)
            } catch (errorException: Exception) {
                errorException.printStackTrace()
            }
        }
    }

}