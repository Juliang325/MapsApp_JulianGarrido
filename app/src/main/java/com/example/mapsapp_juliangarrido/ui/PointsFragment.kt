package com.example.mapsapp_juliangarrido.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapsapp_juliangarrido.R
import com.example.mapsapp_juliangarrido.databinding.FragmentPointsBinding
import com.example.mapsapp_juliangarrido.model.Point
import com.example.mapsapp_juliangarrido.ui.adapter.PointAdapter

class PointsFragment : Fragment() {

    private var _binding: FragmentPointsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewmodel by activityViewModels()
    private lateinit var pointAdapter: PointAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPointsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initListeners()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rvPoints)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observa la lista de puntos en el SharedViewModel y actualiza el adapter
        viewModel.puntoMutableList.observe(viewLifecycleOwner) { pointList ->
            pointAdapter = PointAdapter(pointList, onClickDelete = { position -> showDeleteConfirmationDialog(position) })
            recyclerView.adapter = pointAdapter
        }
    }

    fun initListeners(){
        binding.btnAdd.setOnClickListener{
            showCoordinateInputDialog()
        }
    }
    /********  BORRAR PUNTO   **********/
    //Mostrar alertDialogo para confirmar el borrado de alimento
    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de que quieres borrarlo?")

        builder.setPositiveButton("Sí") { _, _ ->
            onClickDelete(position)
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        val dialog = builder.create()
        dialog.show()
    }
    //Logica para borrar elemento de la lista
    private fun onClickDelete(position: Int) {
        if (position >= 0 && position < (viewModel.puntoMutableList.value?.size ?: 0)) {
            viewModel.deleteAtPosition(position)
        }
    }
    private fun showCoordinateInputDialog() {
        val inputLayout = LinearLayout(context)
        inputLayout.orientation = LinearLayout.VERTICAL
        val inputNombre = EditText(context)
        val inputLatitud = EditText(context)
        val inputLongitud = EditText(context)
        inputNombre.hint = "Nombre del punto"
        inputLatitud.hint = "Latitud"
        inputLongitud.hint = "Longitud"
        inputLayout.setPadding(50, 0, 50, 0)
        inputLayout.addView(inputNombre)
        inputLayout.addView(inputLatitud)
        inputLayout.addView(inputLongitud)

        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle("Introducir Coordenadas")
            .setView(inputLayout)
            .setPositiveButton("Aceptar") { _, _ ->
                val nombre = inputNombre.text.toString()
                val latitud = inputLatitud.text.toString().toDouble()
                val longitud = inputLongitud.text.toString().toDouble()
                viewModel.addPoint(Point(nombre, latitud, longitud))
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }
}

