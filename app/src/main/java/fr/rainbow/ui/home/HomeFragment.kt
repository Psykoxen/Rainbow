package fr.rainbow.ui.home

import android.app.Activity
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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.gson.Gson
import fr.rainbow.BuildConfig
import fr.rainbow.DetailedActivity
import fr.rainbow.MainActivity
import fr.rainbow.R
import fr.rainbow.adapters.FavoriteAdapter
import fr.rainbow.databinding.FragmentHomeBinding
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.dataclasses.Position
import fr.rainbow.dataclasses.WeatherData
import kotlinx.android.synthetic.main.item_favorite.view.*
import kotlinx.android.synthetic.main.item_favorite_big.*
import kotlinx.android.synthetic.main.item_favorite_big.view.*
import okhttp3.*
import java.io.IOException
import kotlin.concurrent.thread


class HomeFragment : Fragment() {
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
            adapter = FavoriteAdapter(favorites, context){
                favorite ->  openYourActivity(favorite)
            }
        }
        initAllData()

        return root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    fun initAllData(){
        favorites.forEachIndexed { index, favorite ->
            if(favorite.latitude==0.0 && favorite.longitude==0.0){

            }else{
                requestMainSection("https://api.open-meteo.com/v1/forecast?latitude=${favorite.latitude}&longitude=${favorite.longitude}&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_probability_max,sunrise,sunset&timezone=auto",index)
            }
        }
    }


    fun requestMainSection(url: String,index: Int) {
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
                favorites[index].weatherData = weatherData
                if(favorites[index].name == "Your Position"){
                    val key = BuildConfig.GOOGLE_MAPS_API_KEY
                    requestYourPosition("https://maps.googleapis.com/maps/api/geocode/json?key=$key&latlng=${favorites[index].latitude},${favorites[index].longitude}",index)
                }
                activity?.runOnUiThread {
                    if(favorites[index].weatherData!=null) {
                        recyclerView.adapter!!.notifyDataSetChanged()

                    }else{
                        Log.e("ERROR","Error when trying to display data")
                    }

                }
            }

        })
    }

    fun requestYourPosition(url: String,index:Int) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("GPS","Error API reverseGeocoding request")
            }
            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                val position = gson.fromJson(response.body()?.string(), Position::class.java )
                if (position.plus_code != null){
                    var temp = position.plus_code.compound_code

                    temp = temp.substring(temp.indexOf(" "))
                    temp = temp.substring(0,temp.indexOf(","))
                    Log.d("NAME",temp)
                    favorites[index].name = temp
                    activity?.runOnUiThread {
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }else{
                    Log.e("GPS","No Name for location")
                }

            }

        })
    }



    //GPS
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            locationResult.lastLocation?.let { locationChanged(it) }
            gpsFavorite.latitude = locationResult.lastLocation?.latitude!!
            gpsFavorite.longitude = locationResult.lastLocation?.longitude!!

            val index = favorites.indexOf(gpsFavorite)
            requestMainSection("https://api.open-meteo.com/v1/forecast?latitude=${favorites[index].latitude}&longitude=${favorites[index].longitude}&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_probability_max,sunrise,sunset&timezone=auto",index)
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
    }

    @Deprecated("Deprecated in Java")
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

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val favorite = (data?.getSerializableExtra("favorite") as? Favorite)!!
            Log.d("test", favorite.toString())
        }
    }
    fun openYourActivity(favoriteItem: Favorite) {
        val detailedIntent = Intent(context, DetailedActivity::class.java)
        detailedIntent.putExtra("favorite",favoriteItem)
        resultLauncher.launch(detailedIntent)
    }
}