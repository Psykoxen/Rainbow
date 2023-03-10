package fr.rainbow.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.gson.Gson
import fr.rainbow.BuildConfig
import fr.rainbow.R
import fr.rainbow.adapters.DetailedHourlyAdapter
import fr.rainbow.adapters.FavoriteAdapter
import fr.rainbow.dataclasses.WeatherData
import fr.rainbow.databinding.FragmentHomeBinding
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.dataclasses.HourWeatherData
import fr.rainbow.dataclasses.Position
import fr.rainbow.functions.file.updatingTempValue
import fr.rainbow.functions.file.updatingWeatherIc
import fr.rainbow.ui.detailed.DetailedActivity
import kotlinx.android.synthetic.main.activity_detailed.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.temperature_now_value
import kotlinx.android.synthetic.main.fragment_home.weather_icon
import okhttp3.*
import java.io.IOException
import java.time.LocalDateTime


class HomeFragment : Fragment() {
    private val client = OkHttpClient()
    private var _binding: FragmentHomeBinding? = null

    //GPS
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private var name = "Your Position";
    private val interval: Long = 10000 // 10seconds
    private val fastestInterval: Long = 5000 // 5 seconds
    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val requestPermissionCode = 999

    private val favorites : ArrayList<Favorite> = ArrayList()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        //GPS
        fusedLocationProviderClient = this.activity?.let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }
        mLocationRequest = LocationRequest.create()
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlertMessage()
        }
        this.activity?.let { checkForPermission(it) }
        startLocationUpdates()

        /**val fav1 = Favorite("Villeurbane",45.786,4.883)
        val fav2 = Favorite("somewhere",42.0,6.0)
        favorites.add(fav1)
        favorites.add(fav2)

        val recyclerView = hourView
        with(recyclerView) {
            layoutManager = LinearLayoutManager(this.context)
            adapter = FavoriteAdapter(favorites, context)
        }**/

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        main_section.setOnClickListener {
            val detailedIntent = Intent(requireContext(), DetailedActivity::class.java)
            detailedIntent.putExtra("latitude", latitude.toString())
            detailedIntent.putExtra("longitude", longitude.toString())
            detailedIntent.putExtra("name",name)
            startActivity(detailedIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
    }

    fun requestMainSection(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("DATA","error api request")
            }
            override fun onResponse(call: Call, response: Response) {
                //response.body()?.let { Log.d("DATA", it.string()) }
                val gson = Gson()
                val weatherData = gson.fromJson(response.body()?.string(), WeatherData::class.java )
                Log.d("DATA",weatherData.daily.weathercode.toString())
                if(city_label.text == "Your Position"){
                    val key = BuildConfig.apiMaps
                    requestYourPosition("https://maps.googleapis.com/maps/api/geocode/json?key=$key&latlng=$latitude,$longitude")
                }
                activity?.runOnUiThread {
                    updatingTempValue(tmp_min_value,weatherData.daily.temperature_2m_min[0])
                    updatingTempValue(tmp_max_value,weatherData.daily.temperature_2m_max[0])
                    updatingWeatherIc(weather_icon,weatherData.daily.weathercode[0])
                    updatingTempValue(temperature_now_value,weatherData.hourly.temperature_2m[findCurrentSlotHourly(weatherData)])
                    rain_probability.progress = weatherData.hourly.precipitation_probability[findCurrentSlotHourly(weatherData)]
                }
            }

        })
    }

    fun requestYourPosition(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("DATA","error api reverseGeocoding request")
            }
            override fun onResponse(call: Call, response: Response) {
                //response.body()?.let { Log.d("DATA", it.string()) }
                val gson = Gson()
                val position = gson.fromJson(response.body()?.string(), Position::class.java )
                Log.d("DATA",position.toString())
                var temp = position.plus_code.compound_code
                temp = temp.substring(temp.indexOf(" "))
                name = temp
                activity?.runOnUiThread {
                    city_label.text = temp
                }
            }

        })
    }

    fun findCurrentSlotHourly(weatherData: WeatherData): Int {
        val current = LocalDateTime.now()
        for (i in 0..weatherData.hourly.time.size) {
            if (weatherData.hourly.time[i] < current.toString()) {
                if (weatherData.hourly.time[i+1] > current.toString()) {
                    return i
                }
            }
        }
        return -1
    }

    //GPS
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            Log.d("MainActivity", "callback: $latitude $longitude")
            locationResult.lastLocation?.let { locationChanged(it) }
            latitude = locationResult.lastLocation?.latitude!!
            longitude = locationResult.lastLocation?.longitude!!
            requestMainSection("https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_probability_max&timezone=Europe%2FBerlin")

            Log.d("GPS","latitude : $latitude longitude : $longitude")
        }
    }

    private fun startLocationUpdates() {
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = interval
        mLocationRequest.fastestInterval = fastestInterval

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()
        val settingsClient = this.activity?.let { LocationServices.getSettingsClient(it) }
        if (settingsClient != null) {
            settingsClient.checkLocationSettings(locationSettingsRequest)
        }

        fusedLocationProviderClient = this.activity?.let {
            LocationServices.getFusedLocationProviderClient(
                it
            )
        }

        if (this.activity?.let {
                ActivityCompat.checkSelfPermission(
                    it, android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && this.activity?.let {
                ActivityCompat.checkSelfPermission(
                    it, android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()!!)
    }

    private fun checkForPermission(context: Context) {
        if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            return
        } else {
            this.activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    requestPermissionCode)
            }
            return
        }
    }

    private fun showAlertMessage() {
        val builder = AlertDialog.Builder(this.activity)
        builder.setMessage("The location permission is disabled. Do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 10)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()

            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun locationChanged(location: Location) {
        mLastLocation = location
        longitude = mLastLocation.longitude
        latitude = mLastLocation.latitude
        Log.d("GPS", "function: $latitude $longitude")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestPermissionCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(this.activity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}