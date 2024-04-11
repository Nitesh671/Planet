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
import com.example.planets.databinding.VehicleDetailActivityBinding
import com.example.planets.model.PlanetViewModel

class SpeciesDetailActivity: ComponentActivity() {
    private lateinit var viewBinding: VehicleDetailActivityBinding
    private lateinit var speciesHeader: TextView
    private lateinit var speciesDetails: TextView
    private lateinit var homeworldPlanet: TextView

    private lateinit var residentHeader: TextView
    private lateinit var residentSubHeader: TextView
    private lateinit var residentAdapter: ResidentAdapter
    lateinit var rvResident: RecyclerView

    private lateinit var filmHeader: TextView
    private lateinit var filmSubHeader: TextView
    private lateinit var filmAdapter: ResidentAdapter
    lateinit var rvFilm: RecyclerView

    private val planetViewModel: PlanetViewModel by viewModels()

    var homeworld = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = VehicleDetailActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.vehicle_detail_activity)

        speciesHeader = findViewById(R.id.vehicleHeader)
        speciesDetails = findViewById(R.id.vehicle_details)
        homeworldPlanet = findViewById(R.id.homeworld)

        rvResident = findViewById(R.id.rv_character_list)
        residentAdapter = ResidentAdapter()
        residentHeader = findViewById(R.id.character_header)
        residentSubHeader = findViewById(R.id.character_info)

        rvFilm = findViewById(R.id.rv_film_list)
        filmAdapter = ResidentAdapter()
        filmHeader = findViewById(R.id.film_header)
        filmSubHeader = findViewById(R.id.film_info)

        var speciesId = intent.getStringExtra(PlanetDetailsActivity.ID)
        speciesId = speciesId?.substringAfterLast("species/")

        if (speciesId != null) {
            planetViewModel.fetchSpecies(speciesId)
        }

        rvResident.layoutManager = LinearLayoutManager(this@SpeciesDetailActivity)
        rvResident.adapter = residentAdapter

        rvFilm.layoutManager = LinearLayoutManager(this@SpeciesDetailActivity)
        rvFilm.adapter = filmAdapter
    }

    override fun onResume() {
        super.onResume()
        speciesHeader.text = getString(R.string.species_details)
        observer()
        onClickListener()
    }

    fun observer() {
        planetViewModel.apply {
            speciesData.observe(this@SpeciesDetailActivity) { data ->

                val html = "<p>Name: ${data.name}</p>" +
                        "<p>Classification: ${data.classification}</p>" +
                        "<p>Designation: ${data.designation}</p>" +
                        "<p>Average Height: ${data.averageHeight}</p>" +
                        "<p>Skin Colors: ${data.skin_colors}</p>" +
                        "<p>Hair Colors: ${data.hair_colors}</p>" +
                        "<p>Eye Colors: ${data.eye_colors}</p>" +
                        "<p>Average Lifespan: ${data.average_lifespan}</p>" +
                        "<p>Language: ${data.language}</p>"

                speciesDetails.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

                if(data.homeworld.isNullOrEmpty()) {
                    homeworldPlanet.visibility = View.GONE
                } else {
                    homeworld = data.homeworld
                    var planet = data.homeworld.substringAfterLast("planets/")
                    planet = planet.substring(0, planet.length - 1)
                    homeworldPlanet.text = "Homeworld: ${planet} (click to see details)"
                    homeworldPlanet.visibility = View.VISIBLE
                }

                if(data.people.isNullOrEmpty()) {
                    rvResident.visibility = View.GONE
                    residentHeader.visibility = View.GONE
                    residentSubHeader.visibility = View.GONE
                } else {
                    residentHeader.text = getString(R.string.people)
                    rvResident.visibility = View.VISIBLE
                    residentHeader.visibility = View.VISIBLE
                    residentSubHeader.visibility = View.VISIBLE
                    data?.people?.let { residentAdapter.updateList(it) }
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
            }
        }
    }

    fun onClickListener() {
        homeworldPlanet.setOnClickListener{
            val intent = Intent(this@SpeciesDetailActivity, PlanetDetailsActivity::class.java)
            intent.putExtra(PlanetDetailsActivity.ID, homeworld)
            startActivity(intent)
        }

        adapterClickAction(residentAdapter, this@SpeciesDetailActivity, ResidentDetailActivity())
        adapterClickAction(filmAdapter, this@SpeciesDetailActivity, FilmDetailActivity())
    }

    fun adapterClickAction(adapter: ResidentAdapter, context: Context, activity: Activity) {
        adapter.setOnClickListener(object :
            ResidentAdapter.OnClickListener {
            override fun onClick(position: Int, data: String) {
                Log.i("Nitesh adapter", "$position")
                val intent =
                    Intent(context, activity::class.java)
                intent.putExtra(PlanetDetailsActivity.ID, data)
                startActivity(intent)
            }
        })
    }
}