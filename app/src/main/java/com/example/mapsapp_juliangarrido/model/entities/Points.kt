package com.example.mapsapp_juliangarrido.model.entities

import com.example.mapsapp_juliangarrido.model.entities.Point
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "points")
data class Points(
    @field:ElementList(inline = true, entry = "point")
    var points: MutableList<Point> = mutableListOf()
)
