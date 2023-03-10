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
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.gson.Gson
import fr.rainbow.BuildConfig
import fr.rainbow.MainActivity
import fr.rainbow.R
import fr.rainbow.adapters.FavoriteAdapter
import fr.rainbow.databinding.FragmentHomeBinding
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.dataclasses.Position
import fr.rainbow.dataclasses.WeatherData
import fr.rainbow.functions.file.updatingTempValue
import fr.rainbow.functions.file.updatingWeatherIc
import kotlinx.android.synthetic.main.item_favorite.view.*
import kotlinx.android.synthetic.main.item_favorite_big.*
import kotlinx.android.synthetic.main.item_favorite_big.view.*
import okhttp3.*
import java.io.IOException
import java.time.LocalDateTime


class HomeFragment() : Fragment() {
    private val client = OkHttpClient()
    private var _binding: FragmentHomeBinding? = null

    //TODO bloquer fonctionnement gps si pas dans la liste de favoris
    //GPS
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val interval: Long = 10000 // 10seconds
    private val fastestInterval: Long = 5000 // 5 seconds
    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val requestPermissionCode = 999

    private lateinit var favorites : ArrayList<Favorite>
    private lateinit var gpsFavorite: Favorite
    private lateinit var recyclerView : RecyclerView

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




        favorites = (activity as MainActivity).favorites
        for (i in favorites){
            if (i.isGPS){
                gpsFavorite = i
            }
        }
        recyclerView = root.findViewById(R.id.favorite_list)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(this.context)
            adapter = FavoriteAdapter(favorites, context)
        }
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        initAllData()

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
    }

    fun initAllData(){
        var index = 0
        for(i in favorites){
            requestMainSection("https://api.open-meteo.com/v1/forecast?latitude=${i.latitude}&longitude=${i.longitude}&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_probability_max&timezone=Europe%2FBerlin",index)
            index++
        }
    }
    fun requestMainSection(url: String,index: Int) {
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
                var weatherData = gson.fromJson(response.body()?.string(), WeatherData::class.java )
                favorites[index].weatherData = weatherData
                if(favorites[index].name == "Your Position"){
                    val key = BuildConfig.apiMaps
                    requestYourPosition("https://maps.googleapis.com/maps/api/geocode/json?key=$key&latlng=${favorites[index].latitude},${favorites[index].longitude}",index)
                }
                activity?.runOnUiThread {
                    if(favorites[index].weatherData!=null) {
                        if (recyclerView[index].city_label2 != null) {
                            updatingTempValue(recyclerView[index].city_label2,favorites[index].name)
                            updatingTempValue(recyclerView[index].temperature_now_value2,favorites[index].weatherData!!.hourly.temperature_2m.get(
                                findCurrentSlotHourly(favorites[index].weatherData)).toString())
                            updatingWeatherIc(recyclerView[index].weather_icon2,favorites[index].weatherData!!.daily.weathercode[0])
                        } else {
                            updatingTempValue(recyclerView[index].city_label,favorites[index].name)
                            updatingTempValue(recyclerView[index].temperature_now_value,favorites[index].weatherData?.hourly?.temperature_2m?.get(
                                findCurrentSlotHourly(favorites[index].weatherData)).toString())
                            updatingWeatherIc(recyclerView[index].weather_icon,
                                favorites[index].weatherData?.daily?.weathercode!![0])
                            updatingTempValue(tmp_min_value, favorites[index].weatherData!!.daily.temperature_2m_min[0])
                            updatingTempValue(tmp_max_value, favorites[index].weatherData!!.daily.temperature_2m_max[0])
                            rain_probability.progress = favorites[index].weatherData!!.hourly.precipitation_probability[findCurrentSlotHourly(favorites[index].weatherData)]
                        }
                    }else{
                        Log.d("ERROR","Error when trying to display data")
                    }

                }
            }

        })
    }

    fun requestYourPosition(url: String,index:Int) {
        Log.d("GPS","url : $url")
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("GPS","error api reverseGeocoding request")
            }
            override fun onResponse(call: Call, response: Response) {
                //response.body()?.let { Log.d("DATA", it.string()) }
                val gson = Gson()
                val position = gson.fromJson(response.body()?.string(), Position::class.java )
                if (position.plus_code!=null){
                    var temp = position.plus_code.compound_code
                    temp = temp.substring(temp.indexOf(" "))
                    favorites[index].name = temp
                }else{
                    Log.d("GPS","No Name for location")
                }

            }

        })
    }

    fun findCurrentSlotHourly(weatherData: WeatherData?): Int {
        if(weatherData!= null){
            val current = LocalDateTime.now()
            for (i in 0..weatherData.hourly.time.size) {
                if (weatherData.hourly.time[i] < current.toString()) {
                    if (weatherData.hourly.time[i+1] > current.toString()) {
                        return i
                    }
                }
            }
        }
        return -1
    }

    //GPS
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            Log.d("MainActivity", "callback: ${gpsFavorite.latitude} ${gpsFavorite.longitude}")
            locationResult.lastLocation?.let { locationChanged(it) }
            gpsFavorite.latitude = locationResult.lastLocation?.latitude!!
            gpsFavorite.longitude = locationResult.lastLocation?.longitude!!

            var index = favorites.indexOf(gpsFavorite)
            requestMainSection("https://api.open-meteo.com/v1/forecast?latitude=${favorites[index].latitude}&longitude=${favorites[index].longitude}&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_probability_max&timezone=Europe%2FBerlin",index)

            Log.d("GPS","latitude : ${gpsFavorite.latitude} longitude : ${gpsFavorite.longitude}")
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
        gpsFavorite.longitude = mLastLocation.longitude
        gpsFavorite.latitude = mLastLocation.latitude
        Log.d("GPS", "function: ${gpsFavorite.latitude} ${gpsFavorite.longitude}")
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