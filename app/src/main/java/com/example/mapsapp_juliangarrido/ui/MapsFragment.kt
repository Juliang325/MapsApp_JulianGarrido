package com.example.mapsapp_juliangarrido.ui

import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.example.mapsapp_juliangarrido.R
import com.example.mapsapp_juliangarrido.model.entities.Point
import com.example.mapsapp_juliangarrido.model.services.SharedViewmodel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
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
                val latLng = LatLng(point.latitud, point.longitud)
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
        val dialog = AlertDialog.Builder(requireContext())
        val input = EditText(requireContext())
        dialog.setView(input)
        dialog.setTitle("Nombre del punto")

        dialog.setPositiveButton("Aceptar") { _, _ ->
            val nombre = input.text.toString()
            points.add(latLng)
            googleMap.addMarker(MarkerOptions().position(latLng).title(nombre))
            Log.d("MapFragment", "Latitud: ${latLng.latitude}, Longitud: ${latLng.longitude}, Nombre: $nombre")
            Log.d("MapFragment", "Lista de puntos: $points")

            viewModel.addPoint(Point(nombre, latLng.latitude, latLng.longitude))

            // Calcular la distancia si hay al menos dos puntos en la lista
            if (points.size >= 2) {
                val distance = calculateDistance(points[points.size - 2], points[points.size - 1])
                val distanceInKm = distance / 1000 // Convertir la distancia a kilómetros
                val message = "Distancia entre los dos últimos puntos: %.2f km".format(distanceInKm)
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }

        dialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun calculateDistance(start: LatLng, end: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results)
        return results[0]
    }
}
