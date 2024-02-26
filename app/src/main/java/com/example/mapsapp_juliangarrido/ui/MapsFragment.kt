package com.example.mapsapp_juliangarrido.ui

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.mapsapp_juliangarrido.R
import com.example.mapsapp_juliangarrido.model.Point

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), GoogleMap.OnMapClickListener {

    private lateinit var googleMap: GoogleMap
    private val viewModel: SharedViewmodel by activityViewModels() // Obtener una instancia del SharedViewModel
    private val points = mutableListOf<LatLng>()

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        googleMap.setOnMapClickListener(this)
        // Obtener la lista de puntos del SharedViewModel y añadir los marcadores al mapa
        viewModel.puntoMutableList.observe(viewLifecycleOwner) { pointList ->
            pointList.forEach { point ->
                val latLng = LatLng(point.latitude, point.longitude)
                googleMap.addMarker(MarkerOptions().position(latLng).title(point.nombre))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onMapClick(latLng: LatLng) {
        points.add(latLng)
        googleMap.addMarker(MarkerOptions().position(latLng).title("New Marker"))
        Log.d("MapFragment", "Latitud: ${latLng.latitude}, Longitud: ${latLng.longitude}")
        Log.d("MapFragment", "Lista de puntos: $points")

        // Agregar el nuevo punto al SharedViewModel
        viewModel.addPoint(Point("New Marker", latLng.latitude, latLng.longitude))
    }
}
