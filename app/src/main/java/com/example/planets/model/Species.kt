package com.example.planets.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Species (
    @SerializedName("name") val name: String,
    @SerializedName("classification") val classification: String,
    @SerializedName("designation") val designation: String,
    @SerializedName("average_height") val averageHeight: String,
    @SerializedName("skin_colors") val skin_colors: String,
    @SerializedName("hair_colors") val hair_colors: String,
    @SerializedName("eye_colors") val eye_colors: String,
    @SerializedName("average_lifespan") val average_lifespan: String,
    @SerializedName("homeworld") val homeworld: String?,
    @SerializedName("language") val language: String,
    @SerializedName("people") val people: ArrayList<String>,
    @SerializedName("films") val films: ArrayList<String>,
    @SerializedName("created") val created: String,
    @SerializedName("edited") val edited: String,
    @SerializedName("url") val url: String
)
