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
import com.example.planets.PlanetDetailsActivity.Companion.VEHICLE
import com.example.planets.Pref.set
import com.example.planets.databinding.VehicleDetailActivityBinding
import com.example.planets.model.PlanetViewModel
import com.example.planets.model.Vehicle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class VehiclesDetailActivity : ComponentActivity() {
    private lateinit var viewBinding: VehicleDetailActivityBinding
    private lateinit var vehicleHeader: TextView
    private lateinit var vehicleDetails: TextView

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
    private var vehicleId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = VehicleDetailActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.vehicle_detail_activity)

        progress = findViewById(R.id.pbProgress)
        progress.visibility = View.VISIBLE

        vehicleHeader = findViewById(R.id.vehicleHeader)
        vehicleDetails = findViewById(R.id.vehicle_details)

        rvResident = findViewById(R.id.rv_character_list)
        residentAdapter = ResidentAdapter(this)
        residentHeader = findViewById(R.id.character_header)
        residentSubHeader = findViewById(R.id.character_info)

        rvFilm = findViewById(R.id.rv_film_list)
        filmAdapter = ResidentAdapter(this)
        filmHeader = findViewById(R.id.film_header)
        filmSubHeader = findViewById(R.id.film_info)

        vehicleId = intent.getStringExtra(PlanetDetailsActivity.ID)?.substringAfterLast("vehicles/")

        if (vehicleId != null) {
            planetViewModel.fetchVehicle(vehicleId!!)
        }

        rvResident.layoutManager = LinearLayoutManager(this@VehiclesDetailActivity)
        rvResident.adapter = residentAdapter

        rvFilm.layoutManager = LinearLayoutManager(this@VehiclesDetailActivity)
        rvFilm.adapter = filmAdapter
        prefs = Pref.defaultPrefs(this)
    }

    override fun onResume() {
        super.onResume()
        vehicleHeader.text = getString(R.string.vehicle_details)
        observer()
        onClickListener()
    }

    fun observer() {
        planetViewModel.apply {
            vehicleData.observe(this@VehiclesDetailActivity) { data ->
                if (data != null) {
                    saveVehicle(data)
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
                            "<p>Vehicle Class: ${data.vehicle_class}</p>"

                    vehicleDetails.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

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
                    getLocalVehicleData(getVehicle())
                    Toast.makeText(this@VehiclesDetailActivity, getString(R.string.connection_issue), Toast.LENGTH_SHORT)
                        .show()
                }
                progress.visibility = View.GONE
            }
        }
    }

    private fun onClickListener() {
        adapterClickAction(residentAdapter, this@VehiclesDetailActivity, ResidentDetailActivity())
        adapterClickAction(filmAdapter, this@VehiclesDetailActivity, FilmDetailActivity())
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

    private fun saveVehicle(vehicle: Vehicle) {
        val gson = Gson()

        val storedHashMapString = prefs.getString(VEHICLE, null)
        val type = object : TypeToken<HashMap<String?, Vehicle?>?>() {}.type
        val hashMap = gson.fromJson<HashMap<String, Vehicle>>(storedHashMapString, type)

        hashMap[vehicleId.toString()] = vehicle
        val vehicleString = gson.toJson(hashMap)
        prefs.set(VEHICLE, vehicleString)
    }

    private fun getVehicle(): Vehicle? {
        val gson = Gson()
        val storedHashMapString = prefs.getString(VEHICLE, null)
        val type = object : TypeToken<HashMap<String?, Vehicle?>?>() {}.type
        val vehicle = gson.fromJson<HashMap<String, Vehicle>>(storedHashMapString, type)
        return vehicle.get(vehicleId.toString())
    }
}