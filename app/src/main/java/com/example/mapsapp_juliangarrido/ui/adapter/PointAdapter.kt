package com.example.mapsapp_juliangarrido.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsapp_juliangarrido.R
import com.example.mapsapp_juliangarrido.model.entities.Point

class PointAdapter(
    private val points: List<Point>,
    private val onClickDelete: (Int) -> Unit
) : RecyclerView.Adapter<PointViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PointViewHolder(layoutInflater.inflate(R.layout.item_point, parent, false))
    }


    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        val item = points[position]
        holder.render(item, onClickDelete)
    }


    override fun getItemCount(): Int = points.size
}