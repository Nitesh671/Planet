package com.example.planets

import android.app.Activity
import android.content.Context
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
import com.example.planets.databinding.FilmDetailActivityBinding
import com.example.planets.model.PlanetViewModel

class FilmDetailActivity: ComponentActivity() {

    private lateinit var viewBinding: FilmDetailActivityBinding
    private lateinit var filmDetails: TextView

    private lateinit var residentHeader: TextView
    private lateinit var residentSubHeader: TextView
    private lateinit var residentAdapter: ResidentAdapter
    lateinit var rvResident: RecyclerView

    private lateinit var planetHeader: TextView
    private lateinit var planetSubHeader: TextView
    private lateinit var planetAdapter: ResidentAdapter
    lateinit var rvPlanet: RecyclerView

    private lateinit var starshipHeader: TextView
    private lateinit var starshipSubHeader: TextView
    private lateinit var starshipAdapter: ResidentAdapter
    lateinit var rvStarship: RecyclerView

    private lateinit var vehiclesHeader: TextView
    private lateinit var vehiclesSubHeader: TextView
    private lateinit var vehiclesAdapter: ResidentAdapter
    lateinit var rvVehicles: RecyclerView

    private lateinit var speciesHeader: TextView
    private lateinit var speciesSubHeader: TextView
    private lateinit var speciesAdapter: ResidentAdapter
    lateinit var rvSpecies: RecyclerView

    private val planetViewModel: PlanetViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = FilmDetailActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.film_detail_activity)

        filmDetails = findViewById(R.id.film_details)

        rvResident = findViewById(R.id.rv_character_list)
        residentAdapter = ResidentAdapter()
        residentHeader = findViewById(R.id.character_header)
        residentSubHeader = findViewById(R.id.character_info)

        rvPlanet = findViewById(R.id.rv_planet_list)
        planetAdapter = ResidentAdapter()
        planetHeader = findViewById(R.id.planetheader)
        planetSubHeader = findViewById(R.id.planet_info)

        rvStarship = findViewById(R.id.rv_starships_list)
        starshipAdapter = ResidentAdapter()
        starshipHeader = findViewById(R.id.starships_header)
        starshipSubHeader = findViewById(R.id.starships_info)

        rvVehicles = findViewById(R.id.rv_vehicles_list)
        vehiclesAdapter = ResidentAdapter()
        vehiclesHeader = findViewById(R.id.vehicles_header)
        vehiclesSubHeader = findViewById(R.id.vehicles_info)

        rvSpecies = findViewById(R.id.rv_species_list)
        speciesAdapter = ResidentAdapter()
        speciesHeader = findViewById(R.id.species_header)
        speciesSubHeader = findViewById(R.id.species_info)

        var filmID = intent.getStringExtra(ID)
        filmID = filmID?.substringAfterLast("films/")

        if (filmID != null) {
            planetViewModel.fetchFilm(filmID)
        }

        rvResident.layoutManager = LinearLayoutManager(this@FilmDetailActivity)
        rvResident.adapter = residentAdapter

        rvPlanet.layoutManager = LinearLayoutManager(this@FilmDetailActivity)
        rvPlanet.adapter = planetAdapter

        rvStarship.layoutManager = LinearLayoutManager(this@FilmDetailActivity)
        rvStarship.adapter = starshipAdapter

        rvVehicles.layoutManager = LinearLayoutManager(this@FilmDetailActivity)
        rvVehicles.adapter = vehiclesAdapter

        rvSpecies.layoutManager = LinearLayoutManager(this@FilmDetailActivity)
        rvSpecies.adapter = speciesAdapter
    }

    override fun onResume() {
        super.onResume()
        observer()
        onClickListener()
    }

    fun observer() {
        planetViewModel.apply {
            filmData.observe(this@FilmDetailActivity) {data->
                val html = "<p>Title: ${data.title}</p>" +
                        "<p>Episode number: ${data.episodeId}</p>" +
                        "<p>Opening Crawl: ${data.openingCrawl}</p>" +
                        "<p>Director: ${data.director}</p>" +
                        "<p>Producer: ${data.producer}</p>" +
                        "<p>Release Data: ${data.releaseDate}</p>"

                filmDetails.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

                if(data.characters.isNullOrEmpty()) {
                    rvResident.visibility = View.GONE
                    residentHeader.visibility = View.GONE
                    residentSubHeader.visibility = View.GONE
                } else {
                    rvResident.visibility = View.VISIBLE
                    residentHeader.visibility = View.VISIBLE
                    residentSubHeader.visibility = View.VISIBLE
                    data?.characters?.let { residentAdapter.updateList(it) }
                }

                if(data.planets.isNullOrEmpty()) {
                    rvPlanet.visibility = View.GONE
                    planetHeader.visibility = View.GONE
                    planetSubHeader.visibility = View.GONE
                } else {
                    rvPlanet.visibility = View.VISIBLE
                    planetHeader.visibility = View.VISIBLE
                    planetSubHeader.visibility = View.VISIBLE
                    data?.planets?.let { planetAdapter.updateList(it) }
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
            }
        }
    }

    fun onClickListener() {
        adapterClickAction(residentAdapter, this@FilmDetailActivity, ResidentDetailActivity())
        adapterClickAction(planetAdapter, this@FilmDetailActivity, PlanetDetailsActivity())
        adapterClickAction(starshipAdapter, this@FilmDetailActivity, StarshipDetailActivity())
        adapterClickAction(vehiclesAdapter, this@FilmDetailActivity, VehiclesDetailActivity())
        adapterClickAction(speciesAdapter, this@FilmDetailActivity, SpeciesDetailActivity())
    }

    fun adapterClickAction(adapter: ResidentAdapter, context: Context, activity: Activity) {
        adapter.setOnClickListener(object :
            ResidentAdapter.OnClickListener {
            override fun onClick(position: Int, data: String) {
                Log.i("Nitesh adapter", "$position")
                val intent =
                    Intent(context, activity::class.java)
                intent.putExtra(ID, data)
                startActivity(intent)
            }
        })
    }
}