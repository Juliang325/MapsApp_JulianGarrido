package com.example.mapsapp_juliangarrido.model

import com.example.mapsapp_juliangarrido.model.entities.Point

object PointProvider {
    val pointList = mutableListOf(
        Point("Point 1", 40.7128, -74.0060),
        Point("Point 2", 34.0522, -118.2437),
        Point("Point 3", 41.8781, -87.6298)
        // Agrega más puntos según tus necesidades
    )
}