package com.example.mapsapp_juliangarrido.model.services

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mapsapp_juliangarrido.model.entities.Point
import com.example.mapsapp_juliangarrido.model.daos.DaoSimpleXML

class SharedViewmodel(application: Application) : AndroidViewModel(application) {
    var puntoMutableList = MutableLiveData<MutableList<Point>>()
    private lateinit var daoSimpleXML: DaoSimpleXML

    init {
        daoSimpleXML = DaoSimpleXML(getApplication())
        cargarDatosDesdeXML()
    }

    private fun cargarDatosDesdeXML() {
        val pointsFromXML = daoSimpleXML.procesarArchivoXMLInterno()
        puntoMutableList.value = pointsFromXML
    }

    fun obtenerPoints(): MutableLiveData<MutableList<Point>> {
        return puntoMutableList
    }

    fun addPoint(point: Point) {
        val currentList = puntoMutableList.value
        currentList?.add(point)
        puntoMutableList.value = currentList
        daoSimpleXML.addPoint(point)
    }

    fun deleteAtPosition(posicion: Int) {
        val currentList = puntoMutableList.value
        if (currentList != null && posicion >= 0 && posicion < currentList.size) {
            daoSimpleXML.borrarPoint(currentList[posicion])
            currentList.removeAt(posicion)
            puntoMutableList.value = currentList
        }
    }

    fun vaciarArchivoInterno() {
        daoSimpleXML.vaciarArchivoInterno()
        cargarDatosDesdeXML() // Actualizar los datos después de vaciar el archivo
    }
}
