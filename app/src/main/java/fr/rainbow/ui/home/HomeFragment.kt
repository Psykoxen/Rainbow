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
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.gson.Gson
import fr.rainbow.databinding.FragmentHomeBinding
import fr.rainbow.ui.detailed.DetailedActivity
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import java.io.IOException
import fr.rainbow.functions.Treatment.Companion.findCurrentSlotHourly
import fr.rainbow.functions.Treatment.Companion.updatingTempValue
import fr.rainbow.functions.Treatment.Companion.updatingWeatherIc
import okhttp3.Request


class HomeFragment : Fragment() {
    private val client = OkHttpClient()
    private var _binding: FragmentHomeBinding? = null

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

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


        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        main_section.setOnClickListener {
            val detailedIntent = Intent(requireContext(), DetailedActivity::class.java)
            detailedIntent.putExtra("latitude", longitude)
            detailedIntent.putExtra("longitude", latitude)
            startActivity(detailedIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()

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
}