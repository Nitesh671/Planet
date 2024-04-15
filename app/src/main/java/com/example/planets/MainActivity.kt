package com.example.planets

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planets.PlanetDetailsActivity.Companion.ID
import com.example.planets.PlanetDetailsActivity.Companion.PLANETS
import com.example.planets.Pref.set
import com.example.planets.databinding.MainActivityBinding
import com.example.planets.model.Planet
import com.example.planets.model.PlanetResponse
import com.example.planets.model.PlanetViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.set

class MainActivity : ComponentActivity() {
    private lateinit var planetAdapter: PlanetListAdapter
    private lateinit var viewBinding: MainActivityBinding
    private val planetViewModel: PlanetViewModel by viewModels()
    lateinit var recyclerview: RecyclerView
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var progress: ProgressBar

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.main_activity)
        recyclerview = findViewById(R.id.rv_card)
        nextButton = findViewById(R.id.next)
        previousButton = findViewById(R.id.previous)
        progress = findViewById(R.id.pbProgress)
        progress.visibility = View.VISIBLE
        planetAdapter = PlanetListAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerview.adapter = planetAdapter
        planetViewModel.fetchPlanets()
        prefs = Pref.defaultPrefs(this)
    }

    override fun onResume() {
        super.onResume()
        observer()
        clickListener()
    }

    private fun observer() {
        planetViewModel.apply {
            planetLiveDate.observe(this@MainActivity) { data ->
                if (data != null) {
                    savePlanets(data)
                    if (data.previous.isNullOrEmpty()) {
                        previousButton.setEnabled(false)
                        previousButton.visibility = View.GONE
                    } else {
                        previousButton.setEnabled(true)
                        previousButton.visibility = View.VISIBLE
                    }
                    if (data.next.isNullOrEmpty()) {
                        nextButton.setEnabled(false)
                        nextButton.visibility = View.GONE
                    } else {
                        nextButton.setEnabled(true)
                        nextButton.visibility = View.VISIBLE
                    }

                    nextButton.setOnClickListener {
                        data.next?.substringAfterLast("=")
                            ?.let { it1 -> planetViewModel.changePage(it1) }
                    }
                    previousButton.setOnClickListener {
                        data.previous?.substringAfterLast("=")
                            ?.let { it1 -> planetViewModel.changePage(it1) }
                    }

                    planetAdapter.updateList(data.results)
                } else {
                    getLocalPlanetsData(getPlanets())
                    Toast.makeText(this@MainActivity, getString(R.string.connection_issue), Toast.LENGTH_SHORT).show()
                }
                progress.visibility = View.GONE
            }
        }
    }

    private fun clickListener() {
        planetAdapter.setOnClickListener(object :
            PlanetListAdapter.OnClickListener {
            override fun onClick(position: Int, data: Planet) {
                val intent = Intent(this@MainActivity, PlanetDetailsActivity::class.java)
                intent.putExtra(ID, data.url)
                startActivity(intent)
            }
        })
    }

    private fun savePlanets(planets: PlanetResponse) {
        val hashMap = HashMap<String, PlanetResponse>()
        hashMap["key1"] = planets
        val gson = Gson()
        val planetString = gson.toJson(hashMap)
        prefs.set(PLANETS, planetString)
    }

    private fun getPlanets(): PlanetResponse? {
        val gson = Gson()
        val storedHashMapString = prefs.getString(PLANETS, null)
        val type = object : TypeToken<HashMap<String?, PlanetResponse?>?>() {}.type
        val planets = gson.fromJson<HashMap<String, PlanetResponse>>(storedHashMapString, type)
        return planets.get("key1")
    }
}