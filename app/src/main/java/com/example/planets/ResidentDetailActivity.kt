package com.example.planets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planets.PlanetDetailsActivity.Companion.ID
import com.example.planets.PlanetDetailsActivity.Companion.RESIDENT
import com.example.planets.Pref.set
import com.example.planets.databinding.ResidentDetailsActivityBinding
import com.example.planets.model.PlanetViewModel
import com.example.planets.model.Resident
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ResidentDetailActivity : ComponentActivity() {

    private lateinit var viewBinding: ResidentDetailsActivityBinding
    private lateinit var residentDetails: TextView
    private lateinit var residentPlanet: TextView

    private lateinit var filmHeader: TextView
    private lateinit var filmSubHeader: TextView
    private lateinit var filmAdapter: ResidentAdapter
    private lateinit var rvFilm: RecyclerView

    private lateinit var speciesHeader: TextView
    private lateinit var speciesSubHeader: TextView
    private lateinit var speciesAdapter: ResidentAdapter
    private lateinit var rvSpecies: RecyclerView

    private lateinit var vehiclesHeader: TextView
    private lateinit var vehiclesSubHeader: TextView
    private lateinit var vehiclesAdapter: ResidentAdapter
    private lateinit var rvVehicles: RecyclerView

    private lateinit var starshipHeader: TextView
    private lateinit var starshipSubHeader: TextView
    private lateinit var starshipAdapter: ResidentAdapter
    private lateinit var rvStarship: RecyclerView

    private var homeworld = ""

    private lateinit var progress: ProgressBar
    private lateinit var prefs: SharedPreferences
    private var residentId: String? = null

    private val planetViewModel: PlanetViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ResidentDetailsActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.resident_details_activity)

        progress = findViewById(R.id.pbProgress)
        progress.visibility = View.VISIBLE

        residentDetails = findViewById(R.id.resident_details)
        residentPlanet = findViewById(R.id.resident_planet)

        rvFilm = findViewById(R.id.rv_film_list)
        filmAdapter = ResidentAdapter(this)
        filmHeader = findViewById(R.id.film_header)
        filmSubHeader = findViewById(R.id.film_info)

        rvSpecies = findViewById(R.id.rv_species_list)
        speciesAdapter = ResidentAdapter(this)
        speciesHeader = findViewById(R.id.species_header)
        speciesSubHeader = findViewById(R.id.species_info)

        rvVehicles = findViewById(R.id.rv_vehicles_list)
        vehiclesAdapter = ResidentAdapter(this)
        vehiclesHeader = findViewById(R.id.vehicles_header)
        vehiclesSubHeader = findViewById(R.id.vehicles_info)

        rvStarship = findViewById(R.id.rv_starships_list)
        starshipAdapter = ResidentAdapter(this)
        starshipHeader = findViewById(R.id.starships_header)
        starshipSubHeader = findViewById(R.id.starships_info)

        residentId = intent.getStringExtra(ID)?.substringAfterLast("people/")

        if (residentId != null) {
            planetViewModel.fetchResident(residentId!!)
        }

        rvFilm.layoutManager = LinearLayoutManager(this@ResidentDetailActivity)
        rvFilm.adapter = filmAdapter

        rvSpecies.layoutManager = LinearLayoutManager(this@ResidentDetailActivity)
        rvSpecies.adapter = speciesAdapter

        rvVehicles.layoutManager = LinearLayoutManager(this@ResidentDetailActivity)
        rvVehicles.adapter = vehiclesAdapter

        rvStarship.layoutManager = LinearLayoutManager(this@ResidentDetailActivity)
        rvStarship.adapter = starshipAdapter

        prefs = Pref.defaultPrefs(this)
    }

    override fun onResume() {
        super.onResume()
        observer()
        onClickListener()
    }

    private fun observer() {
        planetViewModel.apply {
            residentData.observe(this@ResidentDetailActivity) { data ->
                if (data != null) {
                    saveResident(data)
                    val html = "<p>Name: ${data.name}</p>" +
                            "<p>Height: ${data.height}</p>" +
                            "<p>Mass: ${data.mass}</p>" +
                            "<p>Hair Color: ${data.hairColor}</p>" +
                            "<p>Skin Color: ${data.skinColor}</p>" +
                            "<p>Eye Color: ${data.eyeColor}</p>" +
                            "<p>Birth Year: ${data.birthYear}</p>" +
                            "<p>Gender: ${data.gender}</p>"

                    residentDetails.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

                    if (data.homeworld.isNullOrEmpty()) {
                        residentPlanet.visibility = View.GONE
                    } else {
                        homeworld = data.homeworld
                        var planet = data.homeworld.substringAfterLast("planets/")
                        planet = planet.substring(0, planet.length - 1)
                        residentPlanet.text = String.format(getString(R.string.homeworld), planet)
                        residentPlanet.visibility = View.VISIBLE
                    }

                    if (data.films.isEmpty()) {
                        rvFilm.visibility = View.GONE
                        filmHeader.visibility = View.GONE
                        filmSubHeader.visibility = View.GONE
                    } else {
                        rvFilm.visibility = View.VISIBLE
                        filmHeader.visibility = View.VISIBLE
                        filmSubHeader.visibility = View.VISIBLE
                        filmAdapter.updateList(data.films)
                    }

                    if (data.species.isEmpty()) {
                        rvSpecies.visibility = View.GONE
                        speciesHeader.visibility = View.GONE
                        speciesSubHeader.visibility = View.GONE
                    } else {
                        rvSpecies.visibility = View.VISIBLE
                        speciesHeader.visibility = View.VISIBLE
                        speciesSubHeader.visibility = View.VISIBLE
                        speciesAdapter.updateList(data.species)
                    }

                    if (data.vehicles.isEmpty()) {
                        rvVehicles.visibility = View.GONE
                        vehiclesHeader.visibility = View.GONE
                        vehiclesSubHeader.visibility = View.GONE
                    } else {
                        rvVehicles.visibility = View.VISIBLE
                        vehiclesHeader.visibility = View.VISIBLE
                        vehiclesSubHeader.visibility = View.VISIBLE
                        vehiclesAdapter.updateList(data.vehicles)
                    }

                    if (data.starships.isEmpty()) {
                        rvStarship.visibility = View.GONE
                        starshipHeader.visibility = View.GONE
                        starshipSubHeader.visibility = View.GONE
                    } else {
                        rvStarship.visibility = View.VISIBLE
                        starshipHeader.visibility = View.VISIBLE
                        starshipSubHeader.visibility = View.VISIBLE
                        starshipAdapter.updateList(data.starships)
                    }
                } else {
                    getLocalResidentData(getResident())
                    Toast.makeText(this@ResidentDetailActivity, getString(R.string.connection_issue), Toast.LENGTH_SHORT)
                        .show()
                }
                progress.visibility = View.GONE
            }
        }
    }

    private fun onClickListener() {
        residentPlanet.setOnClickListener {
            val intent = Intent(this@ResidentDetailActivity, PlanetDetailsActivity::class.java)
            intent.putExtra(ID, homeworld)
            startActivity(intent)
        }

        adapterClickAction(filmAdapter, this@ResidentDetailActivity, FilmDetailActivity())
        adapterClickAction(speciesAdapter, this@ResidentDetailActivity, SpeciesDetailActivity())
        adapterClickAction(vehiclesAdapter, this@ResidentDetailActivity, VehiclesDetailActivity())
        adapterClickAction(starshipAdapter, this@ResidentDetailActivity, StarshipDetailActivity())
    }

    private fun adapterClickAction(adapter: ResidentAdapter, context: Context, activity: Activity) {
        adapter.setOnClickListener(object :
            ResidentAdapter.OnClickListener {
            override fun onClick(position: Int, data: String) {
                val intent =
                    Intent(context, activity::class.java)
                intent.putExtra(ID, data)
                startActivity(intent)
            }
        })
    }

    private fun saveResident(resident: Resident) {
        val gson = Gson()

        val storedHashMapString = prefs.getString(RESIDENT, null)
        val type = object : TypeToken<HashMap<String?, Resident?>?>() {}.type
        val hashMap = gson.fromJson<HashMap<String, Resident>>(storedHashMapString, type)

        hashMap[residentId.toString()] = resident
        val residentString = gson.toJson(hashMap)
        prefs.set(RESIDENT, residentString)
    }

    private fun getResident(): Resident? {
        val gson = Gson()
        val storedHashMapString = prefs.getString(RESIDENT, null)
        val type = object : TypeToken<HashMap<String?, Resident?>?>() {}.type
        val resident = gson.fromJson<HashMap<String, Resident>>(storedHashMapString, type)
        return resident.get(residentId.toString())
    }
}