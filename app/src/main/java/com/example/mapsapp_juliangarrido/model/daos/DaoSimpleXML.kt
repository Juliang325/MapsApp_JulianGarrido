package com.example.mapsapp_juliangarrido.model.daos

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsapp_juliangarrido.model.entities.Point
import com.example.mapsapp_juliangarrido.model.entities.Points
import org.simpleframework.xml.core.Persister
import java.io.*

class DaoSimpleXML (private val context: Context) {

    fun procesarArchivoAssetsXML(): MutableList<Point> {
        var points: MutableList<Point> = mutableListOf()
        val serializer = Persister()
        var inputStream: InputStream? = null
        var reader: InputStreamReader? = null
        try {
            inputStream = context.assets.open("points.xml")
            reader = InputStreamReader(inputStream)
            val pointsListType = serializer.read(Points::class.java, reader, false)
            //Log.d("pruebaRecetario", recetarioListType.receta.toString())
            points.addAll(pointsListType.points)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                reader?.close()
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        Log.d("Tamaño lista", points.size.toString())
        return points
    }

    fun procesarArchivoXMLInterno():MutableList<Point>{
        var points : MutableList<Point> = mutableListOf()
        val nombreArchivo = "points.xml"
        val serializer = Persister()

        try {
            val file = File(context.filesDir, nombreArchivo)
            val inputStream = FileInputStream(file)
            val pointsList = serializer.read(Points::class.java, inputStream)
            points = pointsList.points
            inputStream.close()
        } catch (e:Exception){
            e.printStackTrace()
        }
        return points
    }

    fun addPoint(point: Point) {
        try {
            val serializer = Persister()
            val points: MutableList<Point> = procesarArchivoXMLInterno() // Obtener las points existentes

            // Agregar la nueva point a la lista existente
            points.add(point)

            // Guardar la lista actualizada en el archivo XML interno
            val recetarioList = Points(points)
            val outputStream = context.openFileOutput("points.xml", AppCompatActivity.MODE_PRIVATE)
            serializer.write(recetarioList, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun borrarPoint(point: Point) {
        try {
            val serializer = Persister()
            val points: MutableList<Point> = procesarArchivoXMLInterno() // Obtener las points existentes

            // Agregar la nueva point a la lista existente
            points.remove(point)

            // Guardar la lista actualizada en el archivo XML interno
            val recetarioList = Points(points)
            val outputStream = context.openFileOutput("points.xml", AppCompatActivity.MODE_PRIVATE)
            serializer.write(recetarioList, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun copiarArchivoDesdeAssets(){
        val nombreArchivo = "points.xml"
        val archivoEnAssets = context.assets.open(nombreArchivo)
        val archivoInterno = context.openFileOutput(nombreArchivo, AppCompatActivity.MODE_PRIVATE)

        archivoEnAssets.copyTo(archivoInterno)
        archivoEnAssets.close()
        archivoInterno.close()
    }

    fun vaciarArchivoInterno(){
        val nombreArchivo = "points.xml"
        val archivoInternoEscritura = context.openFileOutput(nombreArchivo, AppCompatActivity.MODE_PRIVATE)
        archivoInternoEscritura.write("".toByteArray())
        archivoInternoEscritura.close()

    }

}