package com.example.planets

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ResidentAdapter() : RecyclerView.Adapter<ResidentAdapter.ViewHolder>() {

    var listener: OnClickListener? = null
    var residentList: ArrayList<String> = arrayListOf()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView
        val nameLabel: TextView
        init {
            //todo
            name = view.findViewById(R.id.name)
            nameLabel = view.findViewById(R.id.nameLabel)
        }

        fun bind(resident: String, position: Int) {
            nameLabel.text = "Resident $position"
            name.setVisibility(View.GONE)
            Log.i("Nitesh", "bind: ${position}").toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_planet, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("Nitesh2", "bind: $position")
        holder.bind(residentList[position], position)
        holder.itemView.setOnClickListener {
            listener?.onClick(position, residentList[position])
        }
    }

    override fun getItemCount() = residentList.size

    fun updateList(newList: ArrayList<String>) {
        Log.i("Nitesh7", "bind: ${newList[0]}")
        residentList.clear()
        residentList.addAll(newList)
        Log.i("Nitesh4", "bind: ${residentList[0]}")
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onClick(position: Int, data: String)
    }
}