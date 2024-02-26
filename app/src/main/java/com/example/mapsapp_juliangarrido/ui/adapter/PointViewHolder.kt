package com.example.mapsapp_juliangarrido.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsapp_juliangarrido.databinding.ItemPointBinding
import com.example.mapsapp_juliangarrido.model.Point

class PointViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemPointBinding.bind(view)

    fun render(point: Point, onClickDelete: (Int) -> Unit){
        binding.tvName.text = point.nombre
        binding.tvLatitude.text = point.latitude.toString() + " latitud"
        binding.tvLongitude.text = point.longitude.toString() + " longitud"
        binding.cardView.setOnClickListener { onClickDelete(adapterPosition) }
    }
}
