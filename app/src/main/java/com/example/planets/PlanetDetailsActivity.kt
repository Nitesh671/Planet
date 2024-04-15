package com.example.planets

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planets.databinding.PlanetDetailsActivityBinding
import com.example.planets.model.PlanetViewModel

class PlanetDetailsActivity : ComponentActivity() {
    private lateinit var viewBinding: PlanetDetailsActivityBinding
    private val planetViewModel: PlanetViewModel by viewModels()

    private lateinit var name: TextView
    private lateinit var details: TextView
    private lateinit var residentHeader: TextView
    private lateinit var residentSubHeader: TextView
    private lateinit var filmHeader: TextView
    private lateinit var filmSubHeader: TextView

    private lateinit var residentAdapter: ResidentAdapter
    private lateinit var rvResident: RecyclerView
    private lateinit var filmAdapter: ResidentAdapter
    private lateinit var rvFilm: RecyclerView

    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = PlanetDetailsActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.planet_details_activity)
        name = findViewById(R.id.planetname)
        details = findViewById(R.id.details)

        progress = findViewById(R.id.pbProgress)
        progress.visibility = View.VISIBLE

        rvResident = findViewById(R.id.rv_resident_list)
        residentAdapter = ResidentAdapter(this)
        residentHeader = findViewById(R.id.resident_header)
        residentSubHeader = findViewById(R.id.resident_info)

        rvFilm = findViewById(R.id.rv_film_list)
        filmAdapter = ResidentAdapter(this)
        filmHeader = findViewById(R.id.film_header)
        filmSubHeader = findViewById(R.id.film_info)

        var url = intent.getStringExtra(ID)
        url = url?.substringAfterLast("planets/")

        rvResident.layoutManager = LinearLayoutManager(this@PlanetDetailsActivity)
        rvResident.adapter = residentAdapter

        rvFilm.layoutManager = LinearLayoutManager(this@PlanetDetailsActivity)
        rvFilm.adapter = filmAdapter

        if (url != null) {
            planetViewModel.getPlanetData(url)
        }
    }

    override fun onResume() {
        super.onResume()
        observer()
        onClickListener()
    }

    private fun observer() {
        planetViewModel.apply {
            planetDate.observe(this@PlanetDetailsActivity) { data ->
                if (data != null) {
                    name.text = data.name
                    val html = "<p>Rotation Period:  ${data.rotation_period}</p>" +
                            "<p>Orbital Period: ${data.orbital_period}</p>" +
                            "<p>Diameter:  ${data.diameter}</p>" +
                            "<p>Climate: ${data.climate}</p>" +
                            "<p>Gravity: ${data.gravity}</p>" +
                            "<p>Terrian: ${data.terrian}</p>" +
                            "<p>Surface water: ${data.surface_water}</p>" +
                            "<p>Population: ${data.population}</p>" +
                            "<p>Number of residents: ${data.residents.size}</p>" +
                            "<p>Number of films: ${data.films.size}</p>"
                    details.text =
                        Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

                    if (data.residents.isEmpty()) {
                        rvResident.visibility = View.GONE
                        residentHeader.visibility = View.GONE
                        residentSubHeader.visibility = View.GONE
                    } else {
                        rvResident.visibility = View.VISIBLE
                        residentHeader.visibility = View.VISIBLE
                        residentSubHeader.visibility = View.VISIBLE
                        residentAdapter.updateList(data.residents)
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
                    Toast.makeText(this@PlanetDetailsActivity, "API error", Toast.LENGTH_SHORT)
                        .show()
                }
                progress.visibility = View.GONE
            }
        }
    }

    private fun onClickListener() {
        adapterClickAction(residentAdapter, this@PlanetDetailsActivity, ResidentDetailActivity())
        adapterClickAction(filmAdapter, this@PlanetDetailsActivity, FilmDetailActivity())
    }

    private fun adapterClickAction(adapter: ResidentAdapter, context: Context, activity: Activity) {
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

    companion object {
        const val ID = "id"
    }
}