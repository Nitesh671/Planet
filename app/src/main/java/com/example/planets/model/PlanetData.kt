import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanetResponse (
    @SerializedName("count") val count: String,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: ArrayList<Planet>
)

@Serializable
data class Planet(
    @SerialName("name")
    val name: String,
    @SerialName("rotation_period")
    val rotation_period: String,
    @SerialName("oribital_period")
    val orbital_period: String,
    @SerialName("diameter")
    val diameter: String,
    @SerialName("climate")
    val climate: String,
    @SerialName("gravity")
    val gravity: String,
    @SerialName("terrain")
    val terrian: String,
    @SerialName("surface_water")
    val surface_water: String,
    @SerialName("population")
    val population: String,
    @SerialName("residents")
    val residents: ArrayList<String>,
    @SerialName("films")
    val films: ArrayList<String>,
    @SerialName("created")
    val created: String,
    @SerialName("edited")
    val edited: String,
    @SerialName("url")
    val url: String
)