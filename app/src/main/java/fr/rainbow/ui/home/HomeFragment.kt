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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
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
import fr.rainbow.functions.Functions
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.item_favorite.view.*
import kotlinx.android.synthetic.main.item_favorite_big.*
import kotlinx.android.synthetic.main.item_favorite_big.view.*
import okhttp3.*
import java.io.IOException


class HomeFragment : Fragment() {
    private val client = OkHttpClient()
    private var _binding: FragmentHomeBinding? = null

    //GPS
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val interval: Long = 100000 // 10seconds
    private val fastestInterval: Long = 5000 // 5 seconds
    private lateinit var mLastLocation: Location
    private lateinit var mLocationRequest: LocationRequest
    private val requestPermissionCode = 999

    private lateinit var favorites : ArrayList<Favorite>
    private lateinit var gpsFavorite: Favorite
    private var gps = false
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


        favorites = MainActivity.favorites

        val newFavorites =  favorites.toMutableList()

        recyclerView = root.findViewById(R.id.favorite_list)
        /**with(recyclerView) {
            layoutManager = LinearLayoutManager(this.context)
            adapter = FavoriteAdapter(favorites, context){
                favorite ->
                (activity as MainActivity).openYourActivity(favorite)
            }
        }**/

        //
        val adapter = context?.let {
            FavoriteAdapter(favorites, it){ favorite ->
                (activity as MainActivity).openYourActivity(favorite)
            }
        }
        adapter?.differ?.submitList(newFavorites)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        //


        initAllData()

        initGps()
        return root

    }

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                val adapter = recyclerView.adapter as FavoriteAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                adapter.moveItemInRecyclerViewList(from, to)
                adapter.notifyItemMoved(from, to)

                val favorite = favorites[from]
                favorites.removeAt(from)
                favorites.add(to, favorite)
                context?.let { Functions.writeFile(it,favorites) }

                Log.d("test","$favorites")
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }

            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1.0f
            }
        }
        ItemTouchHelper(simpleItemTouchCallback)

    }


    fun initGps(){
        gps = false

        fusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
        for (i in favorites){
            if (i.isGPS){
                gps = true
                gpsFavorite = i
            }
        }
        if(gps){
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

    override fun onResume() {
        super.onResume()
        initGps()
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun initAllData(){
        favorites.forEachIndexed { index, favorite ->
            if(!(favorite.latitude==0.0 && favorite.longitude==0.0)){
                requestMainSection("https://api.open-meteo.com/v1/forecast?latitude=${favorite.latitude}&longitude=${favorite.longitude}&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_probability_max,sunrise,sunset&past_days=2&timezone=auto",index)
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
                if(favorites[index].isGPS){
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
                var temp = position.plus_code.compound_code
                temp = temp.substring(temp.indexOf(" "))
                temp = temp.substring(0,temp.indexOf(",")).replace(" ","")
                favorites[index].name = temp
                activity?.runOnUiThread {
                    recyclerView.adapter!!.notifyDataSetChanged()
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
            if(index!= -1)
                requestMainSection("https://api.open-meteo.com/v1/forecast?latitude=${favorites[index].latitude}&longitude=${favorites[index].longitude}&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_probability_max,sunrise,sunset&past_days=2&timezone=auto",index)
        }
    }

    private fun startLocationUpdates() {
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = interval
        mLocationRequest.fastestInterval = fastestInterval

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()
        activity?.let { LocationServices.getSettingsClient(it) }
            ?.checkLocationSettings(locationSettingsRequest)

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
            Looper.myLooper()!!
        )
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


}