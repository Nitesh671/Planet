package com.example.planets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResidentAdapter(val context: Context) : RecyclerView.Adapter<ResidentAdapter.ViewHolder>() {

    private var listener: OnClickListener? = null
    private var list: ArrayList<String> = arrayListOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView

        init {
            name = view.findViewById(R.id.name)
        }

        fun bind(context: Context, resident: String) {
            if (resident.contains("people")) {
                var id = resident.substringAfterLast("people/")
                id = id.substring(0, id.length - 1)
                name.text = String.format(context.getString(R.string.resident), id)
            } else if (resident.contains("films")) {
                var id = resident.substringAfterLast("films/")
                id = id.substring(0, id.length - 1)
                name.text = String.format(context.getString(R.string.film), id)
            } else if (resident.contains("species")) {
                var id = resident.substringAfterLast("species/")
                id = id.substring(0, id.length - 1)
                name.text = String.format(context.getString(R.string.specie), id)
            } else if (resident.contains("vehicles")) {
                var id = resident.substringAfterLast("vehicles/")
                id = id.substring(0, id.length - 1)
                name.text = String.format(context.getString(R.string.vehicle), id)
            } else if (resident.contains("starships")) {
                var id = resident.substringAfterLast("starships/")
                id = id.substring(0, id.length - 1)
                name.text = String.format(context.getString(R.string.starship), id)
            } else if (resident.contains("planets")) {
                var id = resident.substringAfterLast("planets/")
                id = id.substring(0, id.length - 1)
                name.text = String.format(context.getString(R.string.planet), id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutInflater.from(parent.context).inflate(R.layout.item_planet, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, list[position])
        holder.itemView.setOnClickListener {
            listener?.onClick(position, list[position])
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: ArrayList<String>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onClick(position: Int, data: String)
    }
}