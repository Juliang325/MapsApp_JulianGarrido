package com.example.mapsapp_juliangarrido.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.mapsapp_juliangarrido.R
import com.example.mapsapp_juliangarrido.databinding.FragmentGpsBinding
import com.example.mapsapp_juliangarrido.databinding.FragmentPointsBinding
import com.example.mapsapp_juliangarrido.model.entities.Point
import com.example.mapsapp_juliangarrido.model.services.SharedViewmodel


class GpsFragment : Fragment(), LocationListener {

    private lateinit var locationManager: LocationManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private val viewModel: SharedViewmodel by activityViewModels()
    private var _binding: FragmentGpsBinding? = null
    private val binding get() = _binding!!
    var latitude :Double = 0.0
    var longitude :Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGpsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initListeners()

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            Log.d("Ubicacion", "obteniendo ubicacion")
            obtainLocation()
        }
        return root
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun obtainLocation() {
        Log.d("Ubicacion", "entrano en obtainLocation")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //Esta linea me ha jodido la vida no es GPS_PROVIDER sino NETWORK_PROVIDER
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    obtainLocation()
                } else {
                    // Permiso de ubicación denegado, informar al usuario o tomar medidas alternativas
                    Log.d("Ubicacion", "permisos denegados")
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        // La ubicación actual se encuentra en el objeto 'location'
        latitude = location.latitude
        longitude = location.longitude
        // Utiliza la ubicación como sea necesario
        Log.d("Ubicacion", "Latitud: $latitude, Longitud: $longitude")
    }

    fun initListeners(){
        binding.button.setOnClickListener{
            showCoordinateInputDialog()
        }
    }

    private fun showCoordinateInputDialog() {
        val inputLayout = LinearLayout(context)
        inputLayout.orientation = LinearLayout.VERTICAL
        val inputNombre = EditText(context)
        inputNombre.hint = "Nombre del punto"

        inputLayout.setPadding(50, 0, 50, 0)
        inputLayout.addView(inputNombre)

        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle("Introducir Coordenadas")
            .setView(inputLayout)
            .setPositiveButton("Aceptar") { _, _ ->
                val nombre = inputNombre.text.toString()

                viewModel.addPoint(Point(nombre, latitude, longitude))
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }



}