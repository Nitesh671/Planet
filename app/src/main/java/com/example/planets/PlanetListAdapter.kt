package com.example.planets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planets.model.Planet


class PlanetListAdapter() : RecyclerView.Adapter<PlanetListAdapter.ViewHolder>() {

    private var listener: OnClickListener? = null
    private var planetList: ArrayList<Planet> = arrayListOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView

        init {
            name = view.findViewById(R.id.name)
        }

        fun bind(planet: Planet) {
            name.text = planet.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutInflater.from(parent.context).inflate(R.layout.item_planet, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(planetList[position])
        holder.itemView.setOnClickListener {
            listener?.onClick(position, planetList[position])
        }
    }

    override fun getItemCount() = planetList.size

    fun updateList(newList: ArrayList<Planet>) {
        planetList.clear()
        planetList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onClick(position: Int, data: Planet)
    }
}