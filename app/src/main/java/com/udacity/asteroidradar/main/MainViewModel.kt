package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRadarRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRadarRepository(database)

    private var currentJob: Job? = null


    init {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    val asteroidList= asteroidRepository.asteroids


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}