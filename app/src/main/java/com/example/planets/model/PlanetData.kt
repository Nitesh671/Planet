import com.google.gson.annotations.SerializedName
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
    @SerializedName("name") val name: String,
    @SerializedName("rotation_period") val rotation_period: String,
    @SerializedName("oribital_period") val orbital_period: String,
    @SerializedName("diameter") val diameter: String,
    @SerializedName("climate") val climate: String,
    @SerializedName("gravity") val gravity: String,
    @SerializedName("terrain") val terrian: String,
    @SerializedName("surface_water") val surface_water: String,
    @SerializedName("population") val population: String,
    @SerializedName("residents") val residents: ArrayList<String>,
    @SerializedName("films") val films: ArrayList<String>,
    @SerializedName("created") val created: String,
    @SerializedName("edited") val edited: String,
    @SerializedName("url") val url: String
)