package fr.rainbow.ui.detailed


import WeatherData
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import fr.rainbow.R
import fr.rainbow.databinding.ActivityDetailedBinding
import fr.rainbow.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_detailed.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.temperature_now_value
import kotlinx.android.synthetic.main.fragment_home.weather_icon
import okhttp3.*
import java.io.IOException
import java.time.LocalDateTime


class DetailedActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var binding: ActivityDetailedBinding

    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_return.setOnClickListener {
            finish()
        }

        latitude = intent.getStringExtra("latitude")!!.toDouble()
        longitude = intent.getStringExtra("longitude")!!.toDouble()
        requestData("https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max&timezone=Europe%2FBerlin")

    }


    fun requestData(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("DATA","error api request")
            }
            override fun onResponse(call: Call, response: Response) {
                Log.d("DATA","chui là")
                val gson = Gson()
                val weatherData = gson.fromJson(response.body()?.string(),WeatherData::class.java )
                this@DetailedActivity.runOnUiThread {
                    updatingWeatherIc(weather_icon, weatherData.daily.weathercode[0])
                    updatingTempValue(
                        temperature_now_value,
                        weatherData.hourly.temperature_2m[findCurrentSlotHourly(weatherData)]
                    )
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
}