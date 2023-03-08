package fr.rainbow.ui.home

import WeatherData
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
import com.google.android.gms.location.*
import com.google.gson.Gson
import fr.rainbow.R
import fr.rainbow.databinding.FragmentHomeBinding
import fr.rainbow.ui.detailed.DetailedActivity
import kotlinx.android.synthetic.main.fragment_home.*
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
    private val interval: Long = 10000 // 10seconds
    private val fastestInterval: Long = 5000 // 5 seconds
    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val requestPermissionCode = 999


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

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        main_section.setOnClickListener {
            val detailedIntent = Intent(requireContext(), DetailedActivity::class.java)
            detailedIntent.putExtra("latitude", latitude.toString())
            detailedIntent.putExtra("longitude", longitude.toString())
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
                Log.d("DATA","chui là")
                //response.body()?.let { Log.d("DATA", it.string()) }
                val gson = Gson()
                val weatherData = gson.fromJson(response.body()?.string(),WeatherData::class.java )
                Log.d("DATA",weatherData.daily.weathercode.toString())

                activity?.runOnUiThread {
                    updatingTempValue(tmp_min_value,weatherData.daily.temperature_2m_min[0])
                    updatingTempValue(tmp_max_value,weatherData.daily.temperature_2m_max[0])
                    updatingWeatherIc(weather_icon,weatherData.daily.weathercode[0])
                    updatingTempValue(temperature_now_value,weatherData.hourly.temperature_2m[findCurrentSlotHourly(weatherData)])
                }
            }

        })
    }

    fun updatingTempValue(temp: TextView, value: Any) {
        temp.text = value.toString()
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
    fun updatingWeatherIc(icon: ImageView, value: Any) {
        when(value) {
            0 -> icon.setImageResource(R.drawable.ic_weather_code_0)
            1 -> icon.setImageResource(R.drawable.ic_weather_code_1)
            2 -> icon.setImageResource(R.drawable.ic_weather_code_2_3)
            3 -> icon.setImageResource(R.drawable.ic_weather_code_2_3)
            45 -> icon.setImageResource(R.drawable.ic_weather_code_45_48)
            48 -> icon.setImageResource(R.drawable.ic_weather_code_45_48)
            51 -> icon.setImageResource(R.drawable.ic_weather_code_51_53_55)
            53 -> icon.setImageResource(R.drawable.ic_weather_code_51_53_55)
            55 -> icon.setImageResource(R.drawable.ic_weather_code_51_53_55)
            56 -> icon.setImageResource(R.drawable.ic_weather_code_56_57)
            57 -> icon.setImageResource(R.drawable.ic_weather_code_56_57)
            61 -> icon.setImageResource(R.drawable.ic_weather_code_61_63_65)
            63 -> icon.setImageResource(R.drawable.ic_weather_code_61_63_65)
            65 -> icon.setImageResource(R.drawable.ic_weather_code_61_63_65)
            66 -> icon.setImageResource(R.drawable.ic_weather_code_66_67)
            67 -> icon.setImageResource(R.drawable.ic_weather_code_66_67)
            71 -> icon.setImageResource(R.drawable.ic_weather_code_71_73_75)
            73 -> icon.setImageResource(R.drawable.ic_weather_code_71_73_75)
            75 -> icon.setImageResource(R.drawable.ic_weather_code_71_73_75)
            77 -> icon.setImageResource(R.drawable.ic_weather_code_77)
            80 -> icon.setImageResource(R.drawable.ic_weather_code_80_81_82)
            81 -> icon.setImageResource(R.drawable.ic_weather_code_80_81_82)
            82 -> icon.setImageResource(R.drawable.ic_weather_code_80_81_82)
            95 -> icon.setImageResource(R.drawable.ic_weather_code_95)
            96 -> icon.setImageResource(R.drawable.ic_weather_code_96_99)
            99 -> icon.setImageResource(R.drawable.ic_weather_code_96_99)
            else -> icon.setImageResource(R.drawable.ic_weather_code_0)
        }
    }

    //GPS
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            Log.d("MainActivity", "callback: $latitude $longitude")
            locationResult.lastLocation?.let { locationChanged(it) }
            latitude = locationResult.lastLocation?.latitude!!
            longitude = locationResult.lastLocation?.longitude!!
            requestMainSection("https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max&timezone=Europe%2FBerlin")

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