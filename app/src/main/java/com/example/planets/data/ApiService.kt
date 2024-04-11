import com.example.planets.model.Film
import com.example.planets.model.Resident
import com.example.planets.model.Species
import com.example.planets.model.Starship
import com.example.planets.model.Vehicle
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("planets/")
    suspend fun getPlanets(): PlanetResponse?

    @GET("planets/")
    suspend fun getPage(@Query("page") page: String): PlanetResponse?

    @GET("planets/{id}")
    suspend fun getPlanetData(@Path("id") page: String): Planet?

    @GET("people/{id}")
    suspend fun getResident(@Path("id") id: String): Resident?

    @GET("films/{id}")
    suspend fun getFilm(@Path("id") id: String): Film?

    @GET("vehicles/{id}")
    suspend fun getVehicle(@Path("id") id: String): Vehicle?

    @GET("starships/{id}")
    suspend fun getStarship(@Path("id") id: String): Starship?

    @GET("species/{id}")
    suspend fun getSpecies(@Path("id") id: String): Species?
}