package fr.rainbow.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import fr.rainbow.BuildConfig
import fr.rainbow.R
import fr.rainbow.databinding.FragmentSearchBinding
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.Gson
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.dataclasses.WeatherData
import fr.rainbow.DetailedActivity
import okhttp3.*
import java.io.IOException

class SearchFragment : Fragment() {

    private val client = OkHttpClient()
    private var _binding: FragmentSearchBinding? = null
    private val TAG = "DEBUGMAP"
    private lateinit var tempFavorite:Favorite
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //API Places

        // Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), BuildConfig.GOOGLE_MAPS_API_KEY)
        }

        val searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.d(TAG, place.id)
                Log.d(TAG,place.name)
                val key = BuildConfig.GOOGLE_MAPS_API_KEY
                requestYourPosition("https://maps.googleapis.com/maps/api/geocode/json?place_id=${place.id}&key=$key",place.name)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.d(TAG, "An error occurred: $status")
            }
        })

        return root
    }

    fun requestYourPosition(url: String,name:String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG,"Error API Geocoding request")
            }
            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val place = gson.fromJson(response.body()?.string(), fr.rainbow.dataclasses.Place::class.java )

                val temp = place.results[0].geometry.location
                tempFavorite = Favorite(name, temp.lat ,temp.lng, false, false,null)
                requestMainSection("https://api.open-meteo.com/v1/forecast?latitude=${tempFavorite.latitude}&longitude=${tempFavorite.longitude}&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_probability_max&timezone=Europe%2FBerlin")
            }

        })
    }

    fun requestMainSection(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("API","Error API Request")
            }
            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val weatherData = gson.fromJson(response.body()?.string(), WeatherData::class.java )
                tempFavorite.weatherData = weatherData
                val detailedIntent = Intent(context, DetailedActivity::class.java)
                detailedIntent.putExtra("favorite",tempFavorite)
                context?.let { ContextCompat.startActivity(it, detailedIntent, null) }
            }

        })
    }

}