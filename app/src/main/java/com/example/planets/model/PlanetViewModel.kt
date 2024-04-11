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

    private var _residentData: MutableLiveData<Resident> = MutableLiveData()
    val residentData: LiveData<Resident>
        get() = _residentData

    private var _filmData: MutableLiveData<Film> = MutableLiveData()
    val filmData: LiveData<Film>
        get() = _filmData

    private var _vehicleData: MutableLiveData<Vehicle> = MutableLiveData()
    val vehicleData: LiveData<Vehicle>
        get() = _vehicleData

    private val planetRepository = PlanetRepository()

    fun fetchPlanets() {
        GlobalScope.launch(Dispatchers.IO) {
            val result = planetRepository.getPlanets()
            withContext(Dispatchers.Main) {
                _planetLiveData.value = result
            }
        }
    }

    fun changePage(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = planetRepository.changePage(url)
            withContext(Dispatchers.Main) {
                _planetLiveData.value = result
            }
        }
    }

    fun getPlanetData(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = planetRepository.getPlanetData(url)
            withContext(Dispatchers.Main) {
                _planetData.value = result
            }
        }
    }

    fun fetchResident(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = planetRepository.getResident(url)
            withContext(Dispatchers.Main) {
                _residentData.value = result
            }
        }
    }

    fun fetchFilm(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = planetRepository.getFilm(url)
            withContext(Dispatchers.Main) {
                _filmData.value = result
            }
        }
    }

    fun fetchVehicle(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = planetRepository.getVehicle(url)
            withContext(Dispatchers.Main) {
                _vehicleData.value = result
            }
        }
    }

}