package com.example.planets.model

import Planet
import com.example.planets.repository.PlanetRepository
import PlanetResponse
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlanetViewModel(application: Application): AndroidViewModel(application = application) {
    private var _planetLiveData: MutableLiveData<PlanetResponse> = MutableLiveData()
    val planetLiveDate: LiveData<PlanetResponse>
        get() = _planetLiveData

    private var _planetData: MutableLiveData<Planet> = MutableLiveData()
    val planetDate: LiveData<Planet>
        get() = _planetData

    private val planetRepository = PlanetRepository()

    fun fetchPlanets() {
        GlobalScope.launch(Dispatchers.IO) {
            val planets = planetRepository.getPlanets()
            withContext(Dispatchers.Main) {
                _planetLiveData.value = planets
            }
        }
    }

    fun changePage(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val planets = planetRepository.changePage(url)
            withContext(Dispatchers.Main) {
                _planetLiveData.value = planets
            }
        }
    }

    fun getPlanetData(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val planet = planetRepository.getPlanetData(url)
            withContext(Dispatchers.Main) {
                _planetData.value = planet
            }
        }
    }

}