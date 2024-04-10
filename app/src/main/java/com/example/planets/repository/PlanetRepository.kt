package com.example.planets.repository

import Planet
import PlanetResponse
import RetrofitClient

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
}