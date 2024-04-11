package com.example.planets.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Vehicle (
    @SerializedName("name") val name: String,
    @SerializedName("model") val model: String,
    @SerializedName("manufacturer") val manufacturer: String,
    @SerializedName("cost_in_credits") val costInCredits: String,
    @SerializedName("length") val length: String,
    @SerializedName("max_atmosphering_speed") val maxAtmospheringSpeed: String,
    @SerializedName("crew") val crew: String,
    @SerializedName("passengers") val passengers: String,
    @SerializedName("cargo_capacity") val cargo_capacity: String,
    @SerializedName("consumables") val consumables: String,
    @SerializedName("vehicle_class") val vehicle_class: String,
    @SerializedName("pilots") val pilots: ArrayList<String>,
    @SerializedName("films") val films: ArrayList<String>,
    @SerialName("created") val created: String,
    @SerialName("edited") val edited: String,
    @SerialName("url") val url: String
)
