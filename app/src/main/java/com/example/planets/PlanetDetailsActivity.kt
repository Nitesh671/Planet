package com.example.planets

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planets.MainActivity.Companion.PLANET_ID
import com.example.planets.databinding.PlanetDetailsActivityBinding
import com.example.planets.model.PlanetViewModel

class PlanetDetailsActivity : ComponentActivity() {
    private lateinit var viewBinding: PlanetDetailsActivityBinding
    private val planetViewModel: PlanetViewModel by viewModels()

    private lateinit var name: TextView
    private lateinit var details: TextView
    private lateinit var residentHeader: TextView
    private lateinit var residentSubHeader: TextView

    private lateinit var residentAdapter: ResidentAdapter
    lateinit var recyclerview: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = PlanetDetailsActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.planet_details_activity)
        name = findViewById(R.id.planetname)
        details = findViewById(R.id.details)
        recyclerview = findViewById(R.id.rv_resident_list)
        residentAdapter = ResidentAdapter()
        residentHeader = findViewById(R.id.resident_header)
        residentSubHeader = findViewById(R.id.resident_info)

        var url = getIntent().getStringExtra(PLANET_ID)
        url = url?.substringAfterLast("planets/")

        recyclerview.layoutManager = LinearLayoutManager(this@PlanetDetailsActivity)
        recyclerview.adapter = residentAdapter
        if (url != null) {
            planetViewModel.getPlanetData(url)
        }
    }

    override fun onResume() {
        super.onResume()
        observer()
    }

    fun observer() {
        planetViewModel.apply {
            planetDate.observe(this@PlanetDetailsActivity) {data->
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

                if(data.residents.isNullOrEmpty()) {
                    recyclerview.visibility = View.GONE
                    residentHeader.visibility = View.GONE
                    residentSubHeader.visibility = View.GONE
                } else {
                    recyclerview.visibility = View.VISIBLE
                    residentHeader.visibility = View.VISIBLE
                    residentSubHeader.visibility = View.VISIBLE
                    data?.residents?.let { residentAdapter.updateList(it) }
                }
            }
        }
    }
}