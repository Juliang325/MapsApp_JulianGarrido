package com.example.mapsapp_juliangarrido.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp_juliangarrido.model.Point
import com.example.mapsapp_juliangarrido.model.PointProvider

class SharedViewmodel: ViewModel(){
    var alimentoMutableList = MutableLiveData<MutableList<Point>>()

    init{
        alimentoMutableList.value = PointProvider.pointList
    }

    fun funObtenerPoints(){

    }

    fun addPoint(point: Point){

    }

    fun deletePoint(point: Point){

    }
}