package com.example.planets

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planets.PlanetDetailsActivity.Companion.ID
import com.example.planets.databinding.ResidentDetailsActivityBinding
import com.example.planets.model.PlanetViewModel

class ResidentDetailActivity: ComponentActivity() {

    private lateinit var viewBinding: ResidentDetailsActivityBinding
    private lateinit var residentDetails: TextView
    private lateinit var residentPlanet: TextView

    private lateinit var filmHeader: TextView
    private lateinit var filmSubHeader: TextView
    private lateinit var filmAdapter: ResidentAdapter
    lateinit var rvFilm: RecyclerView

    private lateinit var speciesHeader: TextView
    private lateinit var speciesSubHeader: TextView
    private lateinit var speciesAdapter: ResidentAdapter
    lateinit var rvSpecies: RecyclerView

    private lateinit var vehiclesHeader: TextView
    private lateinit var vehiclesSubHeader: TextView
    private lateinit var vehiclesAdapter: ResidentAdapter
    lateinit var rvVehicles: RecyclerView

    private lateinit var starshipHeader: TextView
    private lateinit var starshipSubHeader: TextView
    private lateinit var starshipAdapter: ResidentAdapter
    lateinit var rvStarship: RecyclerView

    var homeworld = ""

    private val planetViewModel: PlanetViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ResidentDetailsActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.resident_details_activity)

        residentDetails = findViewById(R.id.resident_details)
        residentPlanet = findViewById(R.id.resident_planet)

        rvFilm = findViewById(R.id.rv_film_list)
        filmAdapter = ResidentAdapter()
        filmHeader = findViewById(R.id.film_header)
        filmSubHeader = findViewById(R.id.film_info)

        rvSpecies = findViewById(R.id.rv_species_list)
        speciesAdapter = ResidentAdapter()
        speciesHeader = findViewById(R.id.species_header)
        speciesSubHeader = findViewById(R.id.species_info)

        rvVehicles = findViewById(R.id.rv_vehicles_list)
        vehiclesAdapter = ResidentAdapter()
        vehiclesHeader = findViewById(R.id.vehicles_header)
        vehiclesSubHeader = findViewById(R.id.vehicles_info)

        rvStarship = findViewById(R.id.rv_starships_list)
        starshipAdapter = ResidentAdapter()
        starshipHeader = findViewById(R.id.starships_header)
        starshipSubHeader = findViewById(R.id.starships_info)

        var residentId = intent.getStringExtra(ID)
        residentId = residentId?.substringAfterLast("people/")

        if (residentId != null) {
            planetViewModel.fetchResident(residentId)
        }

        rvFilm.layoutManager = LinearLayoutManager(this@ResidentDetailActivity)
        rvFilm.adapter = filmAdapter

        rvSpecies.layoutManager = LinearLayoutManager(this@ResidentDetailActivity)
        rvSpecies.adapter = speciesAdapter

        rvVehicles.layoutManager = LinearLayoutManager(this@ResidentDetailActivity)
        rvVehicles.adapter = vehiclesAdapter

        rvStarship.layoutManager = LinearLayoutManager(this@ResidentDetailActivity)
        rvStarship.adapter = starshipAdapter
    }

    override fun onResume() {
        super.onResume()
        observer()
        onClickListener()
    }

    private fun observer() {
        planetViewModel.apply {
            residentData.observe(this@ResidentDetailActivity) {data->
                val html = "<p>Name: ${data.name}</p>" +
                        "<p>Height: ${data.height}</p>" +
                        "<p>Mass: ${data.mass}</p>" +
                        "<p>Hair Color: ${data.hairColor}</p>" +
                        "<p>Skin Color: ${data.skinColor}</p>" +
                        "<p>Eye Color: ${data.eyeColor}</p>" +
                        "<p>Birth Year: ${data.birthYear}</p>" +
                        "<p>Gender: ${data.gender}</p>"

                residentDetails.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

                if(data.homeworld.isNullOrEmpty()) {
                    residentPlanet.visibility = View.GONE
                } else {
                    homeworld = data.homeworld
                    var planet = data.homeworld.substringAfterLast("planets/")
                    planet = planet.substring(0, planet.length - 1)
                    residentPlanet.text = "Homeworld: ${planet} (click to see details)"
                    residentPlanet.visibility = View.VISIBLE
                }

                if(data.films.isNullOrEmpty()) {
                    rvFilm.visibility = View.GONE
                    filmHeader.visibility = View.GONE
                    filmSubHeader.visibility = View.GONE
                } else {
                    rvFilm.visibility = View.VISIBLE
                    filmHeader.visibility = View.VISIBLE
                    filmSubHeader.visibility = View.VISIBLE
                    filmAdapter.updateList(data.films)
                }

                if(data.species.isNullOrEmpty()) {
                    rvSpecies.visibility = View.GONE
                    speciesHeader.visibility = View.GONE
                    speciesSubHeader.visibility = View.GONE
                } else {
                    rvSpecies.visibility = View.VISIBLE
                    speciesHeader.visibility = View.VISIBLE
                    speciesSubHeader.visibility = View.VISIBLE
                    speciesAdapter.updateList(data.species)
                }

                if(data.vehicles.isNullOrEmpty()) {
                    rvVehicles.visibility = View.GONE
                    vehiclesHeader.visibility = View.GONE
                    vehiclesSubHeader.visibility = View.GONE
                } else {
                    rvVehicles.visibility = View.VISIBLE
                    vehiclesHeader.visibility = View.VISIBLE
                    vehiclesSubHeader.visibility = View.VISIBLE
                    vehiclesAdapter.updateList(data.vehicles)
                }

                if(data.starships.isNullOrEmpty()) {
                    rvStarship.visibility = View.GONE
                    starshipHeader.visibility = View.GONE
                    starshipSubHeader.visibility = View.GONE
                } else {
                    rvStarship.visibility = View.VISIBLE
                    starshipHeader.visibility = View.VISIBLE
                    starshipSubHeader.visibility = View.VISIBLE
                    starshipAdapter.updateList(data.starships)
                }
            }
        }
    }

    private fun onClickListener() {
        residentPlanet.setOnClickListener{
            val intent = Intent(this@ResidentDetailActivity, PlanetDetailsActivity::class.java)
            intent.putExtra(MainActivity.PLANET_ID, homeworld)
            startActivity(intent)
        }

        filmAdapter.setOnClickListener(object :
            ResidentAdapter.OnClickListener {
            override fun onClick(position: Int, data: String) {
                Log.i("Nitesh adapter", "$position")
                val intent =
                    Intent(this@ResidentDetailActivity, FilmDetailActivity::class.java)
                intent.putExtra(ID, data)
                startActivity(intent)
            }
        })

        speciesAdapter.setOnClickListener(object :
            ResidentAdapter.OnClickListener {
            override fun onClick(position: Int, data: String) {
                Log.i("Nitesh adapter", "$position")
                val intent =
                    Intent(this@ResidentDetailActivity, SpeciesDetailActivity::class.java)
                intent.putExtra(ID, data)
                startActivity(intent)
            }
        })

        vehiclesAdapter.setOnClickListener(object :
            ResidentAdapter.OnClickListener {
            override fun onClick(position: Int, data: String) {
                Log.i("Nitesh adapter", "$position")
                val intent =
                    Intent(this@ResidentDetailActivity, VehiclesDetailActivity::class.java)
                intent.putExtra(ID, data)
                startActivity(intent)
            }
        })

        starshipAdapter.setOnClickListener(object :
            ResidentAdapter.OnClickListener {
            override fun onClick(position: Int, data: String) {
                Log.i("Nitesh adapter", "$position")
                val intent =
                    Intent(this@ResidentDetailActivity, StarshipDetailActivity::class.java)
                intent.putExtra(ID, data)
                startActivity(intent)
            }
        })
    }
}