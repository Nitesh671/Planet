package com.example.planets

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ResidentAdapter() : RecyclerView.Adapter<ResidentAdapter.ViewHolder>() {

    var listener: OnClickListener? = null
    var list: ArrayList<String> = arrayListOf()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView
        init {
            //todo
            name = view.findViewById(R.id.name)
        }

        fun bind(resident: String, position: Int) {
            if(resident.contains("people")) {
                var id = resident.substringAfterLast("people/")
                id = id.substring(0, id.length-1)
                name.text = "Resident ${id}"
            } else if(resident.contains("films")) {
                var id = resident.substringAfterLast("films/")
                id = id.substring(0, id.length-1)
                name.text = "Film ${id}"
            }
            Log.i("Nitesh", "bind: ${position}").toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_planet, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("Nitesh2", "bind: $position")
        holder.bind(list[position], position)
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