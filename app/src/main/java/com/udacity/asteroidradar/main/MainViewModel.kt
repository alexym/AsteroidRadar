package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.constants.Constants
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.AsteroidImg
import com.udacity.asteroidradar.repository.AsteroidRadarRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRadarRepository(database)
    private var currentJob: Job? = null

    private val _navigateToSelected = MutableLiveData<Asteroid>()

    val navigateToSelected: LiveData<Asteroid>
        get() = _navigateToSelected

    init {
        viewModelScope.launch {
            asteroidRepository.getImageOfTheDay()
            asteroidRepository.refreshAsteroids()
        }
    }

    val asteroidList = asteroidRepository.asteroids
    val imgOfTheDay = asteroidRepository.imgOfTheDay

    fun displayDetails(marsProperty: Asteroid) {
        _navigateToSelected.value = marsProperty
    }

    fun displayDetailsComplete() {
        _navigateToSelected.value = null
    }



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