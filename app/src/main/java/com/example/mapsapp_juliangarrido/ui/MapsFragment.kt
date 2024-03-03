package com.example.mapsapp_juliangarrido.ui

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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

            // Agregar el nuevo punto al SharedViewModel
            viewModel.addPoint(Point(nombre, latLng.latitude, latLng.longitude))
        }

        dialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        dialog.show()
    }
}
