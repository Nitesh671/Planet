package com.example.planets

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.planets.PlanetDetailsActivity.Companion.ID
import com.example.planets.databinding.FilmDetailActivityBinding
import com.example.planets.model.PlanetViewModel

class FilmDetailActivity: ComponentActivity() {

    private lateinit var viewBinding: FilmDetailActivityBinding
    private lateinit var filmName: TextView

    private val planetViewModel: PlanetViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = FilmDetailActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.film_detail_activity)

        filmName = findViewById(R.id.film_name)

        var filmID = intent.getStringExtra(ID)
        filmID = filmID?.substringAfterLast("films/")

        if (filmID != null) {
            planetViewModel.fetchFilm(filmID)
        }
    }

    override fun onResume() {
        super.onResume()
        observer()
    }

    fun observer() {
        planetViewModel.apply {
            filmData.observe(this@FilmDetailActivity) {data->
                filmName.text = data.title
            }
        }
    }
}