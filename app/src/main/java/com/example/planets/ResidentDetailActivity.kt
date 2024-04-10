package com.example.planets

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.planets.PlanetDetailsActivity.Companion.ID
import com.example.planets.databinding.ResidentDetailsActivityBinding
import com.example.planets.model.PlanetViewModel

class ResidentDetailActivity: ComponentActivity() {

    private lateinit var viewBinding: ResidentDetailsActivityBinding
    private lateinit var residentName: TextView

    private val planetViewModel: PlanetViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ResidentDetailsActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.resident_details_activity)

        residentName = findViewById(R.id.resident_name)

        var residentId = intent.getStringExtra(ID)
        residentId = residentId?.substringAfterLast("people/")

        if (residentId != null) {
            planetViewModel.fetchResident(residentId)
        }
    }

    override fun onResume() {
        super.onResume()
        observer()
    }

    fun observer() {
        planetViewModel.apply {
            residentData.observe(this@ResidentDetailActivity) {data->
                residentName.text = data.name
            }
        }
    }
}