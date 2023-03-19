package fr.rainbow.ui.map

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import fr.rainbow.MainActivity
import fr.rainbow.R
import fr.rainbow.databinding.FragmentMapBinding
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.dataclasses.MapsData
import fr.rainbow.functions.Functions
import fr.rainbow.functions.Functions.findCurrentSlotHourly
import fr.rainbow.functions.Functions.updatingWeatherBmpIc
import okhttp3.*
import java.io.IOException


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val client = OkHttpClient()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val cities = ArrayListCities()
    private lateinit var favorites : ArrayList<Favorite>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        favorites = MainActivity.favorites
        favorites.forEach { cities.add(Cities(it.name, LatLng(it.latitude, it.longitude))) }

        cities.add(Cities("Paris", LatLng(48.856614, 2.3522219)))
        cities.add(Cities("Lyon", LatLng(45.764043, 4.835659)))
        cities.add(Cities("Marseille", LatLng(43.296482, 5.36978)))
        cities.add(Cities("Toulouse", LatLng(43.604652, 1.444209)))
        cities.add(Cities("Nice", LatLng(43.710173, 7.261953)))
        cities.add(Cities("Nantes", LatLng(47.218371, -1.553621)))
        cities.add(Cities("Montpellier", LatLng(43.611242, 3.876733)))
        cities.add(Cities("Strasbourg", LatLng(48.573405, 7.752111)))
        cities.add(Cities("Bordeaux", LatLng(44.837789, -0.57918)))
        cities.add(Cities("Lille", LatLng(50.62925, 3.057256)))
        cities.add(Cities("Rennes", LatLng(48.117266, -1.677793)))
        cities.add(Cities("Reims", LatLng(49.258329, 4.031696)))
        cities.add(Cities("Le Havre", LatLng(49.493897, 0.107929)))
        cities.add(Cities("Saint-Étienne", LatLng(45.439695, 4.387177)))
        cities.add(Cities("Toulon", LatLng(43.124228, 5.928)))
        cities.add(Cities("Grenoble", LatLng(45.188529, 5.724524)))
        cities.add(Cities("Dijon", LatLng(47.322047, 5.04148)))
        cities.add(Cities("Angers", LatLng(47.473658, -0.551266)))
        cities.add(Cities("Nîmes", LatLng(43.837883, 4.360054)))
        cities.add(Cities("Villeurbanne", LatLng(45.76675, 4.883071)))
        cities.add(Cities("Le Mans", LatLng(48.00611, 0.199556)))
        cities.add(Cities("Aix-en-Provence", LatLng(43.529742, 5.447427)))
        cities.add(Cities("Brest", LatLng(48.390394, -4.486076)))
        cities.add(Cities("Tours", LatLng(47.394144, 0.68484)))
        cities.add(Cities("Amiens", LatLng(49.894067, 2.295753)))
        cities.add(Cities("Limoges", LatLng(45.833619, 1.261105)))
        cities.add(Cities("Clermont-Ferrand", LatLng(45.777222, 3.087025)))
        cities.add(Cities("Perpignan", LatLng(42.688659, 2.894833)))
        cities.add(Cities("Besançon", LatLng(47.237829, 6.024053)))
        cities.add(Cities("Orléans", LatLng(47.902964, 1.909251)))
        cities.add(Cities("Metz", LatLng(49.119358, 6.175394)))
        cities.add(Cities("Rouen", LatLng(49.443232, 1.099971)))
        cities.add(Cities("Mulhouse", LatLng(47.75084, 7.33589)))
        cities.add(Cities("Caen", LatLng(49.18104, -0.370049)))
        cities.add(Cities("Nancy", LatLng(48.692054, 6.184417)))
        cities.add(Cities("Avignon", LatLng(43.949317, 4.805528)))

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync { googleMap ->
            getWeatherCode(googleMap)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cities.get(0).latLng, 8f))
        }

        return root

    }

    private fun getWeatherCode(googleMap: GoogleMap) {
        cities.getAll().forEach() { city ->
            val request = Request.Builder()
                .url("https://api.open-meteo.com/v1/forecast?latitude=${city.latLng.latitude}&longitude=${city.latLng.longitude}&hourly=weathercode")
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("MAPS","Error API reverseGeocoding request on ${city.name}")
                }

                override fun onResponse(call: Call, response: Response) {
                    val gson = Gson()
                    val weatherCode = gson.fromJson(response.body()?.string(), MapsData::class.java)
                    activity?.runOnUiThread {
                        addMarkers2(city, googleMap, weatherCode.hourly.weathercode[findCurrentSlotHourly(weatherCode)])
                    }
                }
            })
        }
    }

    private fun resizeImage(view: View?, bitmap: Bitmap) {
        val resized = Bitmap.createScaledBitmap(bitmap, 400, 400, true)
        //ivImage.setImageBitmap(resized)
    }

    private fun addMarkers2(city: Cities, googleMap: GoogleMap, weatherCode: Int) {
        val marker = googleMap.addMarker(
            MarkerOptions()
                .title(city.name)
                .position(city.latLng)
        )
        updatingWeatherBmpIc(marker, requireContext(), weatherCode)
    }
    private fun addMarkers(googleMap: GoogleMap) {
        cities.getAll().forEach() { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    //.title(place.name)
                    .position(place.latLng)
            )
            Log.d("DEBUGMAP", favorites.find { it.name == place.name }.toString())
            if(favorites.find { it.name == place.name } != null) {
                val fav = favorites.find { it.name == place.name }
                fav?.weatherData?.let { data ->
                    updatingWeatherBmpIc(marker, requireContext(), data.hourly.weathercode[Functions.findCurrentSlotHourly(data)])
                }
            }

            val hourly = "https://api.open-meteo.com/v1/forecast?latitude=45.75&longitude=4.85&hourly=weathercode"
            val daily = "https://api.open-meteo.com/v1/forecast?latitude=45.75&longitude=4.85&daily=weathercode&timezone=auto"
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cities.get(0).latLng, 8f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
data class Cities(val name: String, val latLng: LatLng) {
    override fun equals(other: Any?): Boolean {
        if (other is Cities) {
            return other.name == name
        }
        return false
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + latLng.hashCode()
        return result
    }
}
class ArrayListCities {
    private val citiesList = ArrayList<Cities>()
    fun add(newCities: Cities) {
        citiesList.forEach {
            if (it.name == newCities.name) {
                return
            }
        }
        citiesList.add(newCities)
    }

    fun get(index: Int): Cities {
        return citiesList[index]
    }
    fun getAll(): ArrayList<Cities> {
        return citiesList
    }

}