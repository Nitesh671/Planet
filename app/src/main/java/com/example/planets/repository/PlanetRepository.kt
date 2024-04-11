package com.example.planets.repository

import Planet
import PlanetResponse
import RetrofitClient
import com.example.planets.model.Film
import com.example.planets.model.Resident
import com.example.planets.model.Vehicle

class PlanetRepository {

    private val apiService = RetrofitClient.create()

    suspend fun getPlanets(): PlanetResponse? {
        return try {
            return apiService.getPlanets()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun changePage(url: String): PlanetResponse? {
        return try {
            return apiService.getPage(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getPlanetData(url: String): Planet? {
        return try {
            return apiService.getPlanetData(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getResident(url: String): Resident? {
        return try {
            return apiService.getResident(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getFilm(url: String): Film? {
        return try {
            return apiService.getFilm(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getVehicle(url: String): Vehicle? {
        return try {
            return apiService.getVehicle(url)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}