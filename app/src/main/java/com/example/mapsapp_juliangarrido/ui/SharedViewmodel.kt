package com.example.mapsapp_juliangarrido.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp_juliangarrido.model.Point
import com.example.mapsapp_juliangarrido.model.PointProvider

class SharedViewmodel : ViewModel() {
    var puntoMutableList = MutableLiveData<MutableList<Point>>()

    init {
        puntoMutableList.value = PointProvider.pointList.toMutableList()
    }

    fun funObtenerPoints(): MutableList<Point> {
        val puntos = puntoMutableList.value ?: mutableListOf()
        return puntos
    }

    fun addPoint(point: Point) {
        val currentList = puntoMutableList.value
        currentList?.add(point)
        puntoMutableList.value = currentList
    }

    fun deletePoint(point: Point) {
        val currentList = puntoMutableList.value
        currentList?.remove(point)
        puntoMutableList.value = currentList
    }

    fun deleteAtPosition(posicion: Int) {
        val currentList = puntoMutableList.value
        if (currentList != null && posicion >= 0 && posicion < currentList.size) {
            currentList.removeAt(posicion)
            puntoMutableList.postValue(currentList)
        }
    }
}

