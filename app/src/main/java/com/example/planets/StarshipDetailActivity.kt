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
import com.example.planets.PlanetDetailsActivity.Companion.STARSHIP
import com.example.planets.Pref.set
import com.example.planets.databinding.VehicleDetailActivityBinding
import com.example.planets.model.PlanetViewModel
import com.example.planets.model.Starship
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StarshipDetailActivity : ComponentActivity() {
    private lateinit var viewBinding: VehicleDetailActivityBinding
    private lateinit var starshipHeader: TextView
    private lateinit var starshipDetails: TextView

    private lateinit var residentHeader: TextView
    private lateinit var residentSubHeader: TextView
    private lateinit var residentAdapter: ResidentAdapter
    private lateinit var rvResident: RecyclerView

    private lateinit var filmHeader: TextView
    private lateinit var filmSubHeader: TextView
    private lateinit var filmAdapter: ResidentAdapter
    private lateinit var rvFilm: RecyclerView

    private lateinit var progress: ProgressBar

    private val planetViewModel: PlanetViewModel by viewModels()

    private lateinit var prefs: SharedPreferences
    private var starshipId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = VehicleDetailActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.vehicle_detail_activity)

        progress = findViewById(R.id.pbProgress)
        progress.visibility = View.VISIBLE

        starshipHeader = findViewById(R.id.vehicleHeader)
        starshipDetails = findViewById(R.id.vehicle_details)

        rvResident = findViewById(R.id.rv_character_list)
        residentAdapter = ResidentAdapter(this)
        residentHeader = findViewById(R.id.character_header)
        residentSubHeader = findViewById(R.id.character_info)

        rvFilm = findViewById(R.id.rv_film_list)
        filmAdapter = ResidentAdapter(this)
        filmHeader = findViewById(R.id.film_header)
        filmSubHeader = findViewById(R.id.film_info)

        starshipId =
            intent.getStringExtra(PlanetDetailsActivity.ID)?.substringAfterLast("starships/")

        if (starshipId != null) {
            planetViewModel.fetchStarship(starshipId!!)
        }

        rvResident.layoutManager = LinearLayoutManager(this@StarshipDetailActivity)
        rvResident.adapter = residentAdapter

        rvFilm.layoutManager = LinearLayoutManager(this@StarshipDetailActivity)
        rvFilm.adapter = filmAdapter

        prefs = Pref.defaultPrefs(this)
    }

    override fun onResume() {
        super.onResume()
        starshipHeader.text = getString(R.string.starship_details)
        observer()
        onClickListener()
    }

    private fun observer() {
        planetViewModel.apply {
            starshipData.observe(this@StarshipDetailActivity) { data ->
                if (data != null) {
                    saveStarship(data)
                    val html = "<p>Name: ${data.name}</p>" +
                            "<p>Model: ${data.model}</p>" +
                            "<p>Manufacturer: ${data.manufacturer}</p>" +
                            "<p>Cost in Credits: ${data.costInCredits}</p>" +
                            "<p>Length: ${data.length}</p>" +
                            "<p>Max Atmosphering Speed: ${data.maxAtmospheringSpeed}</p>" +
                            "<p>Crew: ${data.crew}</p>" +
                            "<p>Passengers: ${data.passengers}</p>" +
                            "<p>Cargo Capacity: ${data.cargo_capacity}</p>" +
                            "<p>Consumables: ${data.consumables}</p>" +
                            "<p>Hyperdrive Rating: ${data.hyperdriveRating}</p>" +
                            "<p>MGLT: ${data.MGLT}</p>" +
                            "<p>Vehicle Class: ${data.starshipClass}</p>"

                    starshipDetails.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

                    if (data.pilots.isEmpty()) {
                        rvResident.visibility = View.GONE
                        residentHeader.visibility = View.GONE
                        residentSubHeader.visibility = View.GONE
                    } else {
                        rvResident.visibility = View.VISIBLE
                        residentHeader.visibility = View.VISIBLE
                        residentSubHeader.visibility = View.VISIBLE
                        residentAdapter.updateList(data.pilots)
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
                } else {
                    getLocalStarshipData(getStarship())
                    Toast.makeText(this@StarshipDetailActivity, getString(R.string.connection_issue), Toast.LENGTH_SHORT)
                        .show()
                }
                progress.visibility = View.GONE
            }
        }
    }

    private fun onClickListener() {
        adapterClickAction(residentAdapter, this@StarshipDetailActivity, ResidentDetailActivity())
        adapterClickAction(filmAdapter, this@StarshipDetailActivity, FilmDetailActivity())
    }

    private fun adapterClickAction(adapter: ResidentAdapter, context: Context, activity: Activity) {
        adapter.setOnClickListener(object :
            ResidentAdapter.OnClickListener {
            override fun onClick(position: Int, data: String) {
                val intent =
                    Intent(context, activity::class.java)
                intent.putExtra(PlanetDetailsActivity.ID, data)
                startActivity(intent)
            }
        })
    }

    private fun saveStarship(starship: Starship) {
        val gson = Gson()

        val storedHashMapString = prefs.getString(STARSHIP, null)
        val type = object : TypeToken<HashMap<String?, Starship?>?>() {}.type
        val hashMap = gson.fromJson<HashMap<String, Starship>>(storedHashMapString, type)

        hashMap[starshipId.toString()] = starship
        val starshipString = gson.toJson(hashMap)
        prefs.set(STARSHIP, starshipString)
    }

    private fun getStarship(): Starship? {
        val gson = Gson()
        val storedHashMapString = prefs.getString(STARSHIP, null)
        val type = object : TypeToken<HashMap<String?, Starship?>?>() {}.type
        val starship = gson.fromJson<HashMap<String, Starship>>(storedHashMapString, type)
        return starship.get(starshipId.toString())
    }
}