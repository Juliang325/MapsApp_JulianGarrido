package com.example.mapsapp_juliangarrido.model.entities

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "point")
data class Point(
    @field:Element(name = "nombre")
    var nombre: String = "",

    @field:Element(name = "latitud")
    var latitud: Double = 0.0,

    @field:Element(name = "longitud")
    var longitud: Double = 0.0
)
