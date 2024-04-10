package com.example.planets

import Planet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PlanetListAdapter() : RecyclerView.Adapter<PlanetListAdapter.ViewHolder>() {

    var listener: OnClickListener? = null
    var planetList: ArrayList<Planet> = arrayListOf()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView
        init {
            //todo
            name = view.findViewById(R.id.name)
        }

        fun bind(planet: Planet) {
            name.text = planet.name
            Log.i("Nitesh", "bind: ${planet.name}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_planet, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("Nitesh2", "bind: ${planetList[position].name}")
        holder.bind(planetList[position])
        holder.itemView.setOnClickListener {
            listener?.onClick(position, planetList[position])
        }
    }

    override fun getItemCount() = planetList.size

    fun updateList(newList: ArrayList<Planet>) {
        Log.i("Nitesh7", "bind: ${newList[0].name}")
        planetList.clear()
        planetList.addAll(newList)
        Log.i("Nitesh4", "bind: ${planetList[0].name}")
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onClick(position: Int, data: Planet)
    }
}