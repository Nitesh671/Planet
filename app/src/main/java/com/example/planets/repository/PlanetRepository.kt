package com.example.planets.repository

import Planet
import PlanetResponse
import RetrofitClient
import com.example.planets.model.Film
import com.example.planets.model.Resident
import com.example.planets.model.Species
import com.example.planets.model.Starship
import com.example.planets.model.Vehicle

class PlanetRepository {

    private val apiService = RetrofitClient.create()

    suspend fun getPlanets(): PlanetResponse? {
        return try {
            apiService.getPlanets()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun changePage(url: String): PlanetResponse? {
        return try {
            apiService.getPage(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getPlanetData(url: String): Planet? {
        return try {
            apiService.getPlanetData(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getResident(url: String): Resident? {
        return try {
            apiService.getResident(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getFilm(url: String): Film? {
        return try {
            apiService.getFilm(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getVehicle(url: String): Vehicle? {
        return try {
            apiService.getVehicle(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getStarship(url: String): Starship? {
        return try {
            apiService.getStarship(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getSpecies(url: String): Species? {
        return try {
            apiService.getSpecies(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}