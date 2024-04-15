package com.example.planets

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planets.PlanetDetailsActivity.Companion.ID
import com.example.planets.databinding.MainActivityBinding
import com.example.planets.model.Planet
import com.example.planets.model.PlanetViewModel

class MainActivity : ComponentActivity() {
    private lateinit var planetAdapter: PlanetListAdapter
    private lateinit var viewBinding: MainActivityBinding
    private val planetViewModel: PlanetViewModel by viewModels()
    lateinit var recyclerview: RecyclerView
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var progress: ProgressBar

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
                        val ex = data.next?.substringAfterLast("=")
                        Log.i("Nitesh next", "observer: $ex")
                        data.next?.substringAfterLast("=")
                            ?.let { it1 -> planetViewModel.changePage(it1) }
                    }
                    previousButton.setOnClickListener {
                        val ex = data.previous?.substringAfterLast("=")
                        Log.i("Nitesh previous", "observer: $ex")
                        data.previous?.substringAfterLast("=")
                            ?.let { it1 -> planetViewModel.changePage(it1) }
                    }

                    planetAdapter.updateList(data.results)
                } else {
                    Toast.makeText(this@MainActivity, "API error", Toast.LENGTH_SHORT).show()
                }
                progress.visibility = View.GONE
            }
        }
    }

    private fun clickListener() {
        planetAdapter.setOnClickListener(object :
            PlanetListAdapter.OnClickListener {
            override fun onClick(position: Int, data: Planet) {
                Log.i("Nitesh adapter", "$position")
                val intent = Intent(this@MainActivity, PlanetDetailsActivity::class.java)
                intent.putExtra(ID, data.url)
                startActivity(intent)
            }
        })
    }
}