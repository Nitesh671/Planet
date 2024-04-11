package com.example.planets.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Starship (
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
    @SerializedName("hyperdrive_rating") val hyperdriveRating: String,
    @SerializedName("MGLT") val MGLT: String,
    @SerializedName("starship_class") val starshipClass: String,
    @SerializedName("pilots") val pilots: ArrayList<String>,
    @SerializedName("films") val films: ArrayList<String>,
    @SerializedName("created") val created: String,
    @SerializedName("edited") val edited: String,
    @SerializedName("url") val url: String
)

