package fr.rainbow.ui.detailed


import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fr.rainbow.R
import fr.rainbow.adapters.DetailedDayAdapter
import fr.rainbow.adapters.DetailedHourlyAdapter
import fr.rainbow.dataclasses.WeatherData
import fr.rainbow.databinding.ActivityDetailedBinding
import fr.rainbow.dataclasses.DayWeatherData
import fr.rainbow.dataclasses.HourWeatherData
import kotlinx.android.synthetic.main.activity_detailed.*
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

    private val hourPrevisionList : ArrayList<HourWeatherData> = ArrayList()
    private val dayPrevisionList: ArrayList<DayWeatherData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_return.setOnClickListener {
            finish()
        }

        var recyclerDayView = dayView
        with(recyclerDayView) {
            layoutManager = LinearLayoutManager(this@DetailedActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter = DetailedDayAdapter(dayPrevisionList, context)
        }

        var recyclerHourView = hourView
        with(recyclerHourView) {
            layoutManager = LinearLayoutManager(this@DetailedActivity)
            adapter = DetailedHourlyAdapter(hourPrevisionList, context)
        }

        latitude = intent.getStringExtra("latitude")!!.toDouble()
        longitude = intent.getStringExtra("longitude")!!.toDouble()
        requestData(recyclerDayView,recyclerHourView,"https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall,weathercode,windspeed_10m,winddirection_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,uv_index_max,precipitation_probability_max&timezone=Europe%2FBerlin")


    }

    fun createHoursPrevision(weatherData: WeatherData, recyclerHourView: RecyclerView)
    {
        val index = findCurrentSlotHourly(weatherData)
        for (i in index..index+7)
        {
            hourPrevisionList.add(HourWeatherData(
                weatherData.hourly.time[i].substring(weatherData.hourly.time[i].length-5),
                weatherData.hourly.apparent_temperature[i],
                weatherData.hourly.precipitation[i],
                weatherData.hourly.precipitation_probability[i],
                weatherData.hourly.rain[i],
                weatherData.hourly.relativehumidity_2m[i],
                weatherData.hourly.showers[i],
                weatherData.hourly.snowfall[i],
                weatherData.hourly.temperature_2m[i],
                weatherData.hourly.weathercode[i],
                weatherData.hourly.winddirection_10m[i],
                weatherData.hourly.windspeed_10m[i]))
        }
        recyclerHourView.adapter!!.notifyDataSetChanged()
    }
    fun createDayPrevision(weatherData: WeatherData, recyclerDayView: RecyclerView)
    {

        for (i in 1 until weatherData.daily.time.size)
        {
            dayPrevisionList.add(DayWeatherData(
                weatherData.daily.temperature_2m_max[i],
                weatherData.daily.temperature_2m_min[i],
                weatherData.daily.time[i],
                weatherData.daily.uv_index_max[i],
                weatherData.daily.weathercode[i],
                weatherData.daily.precipitation_probability_max[i])
            )
        }
        recyclerDayView.adapter!!.notifyDataSetChanged()
    }

    fun requestData(dayView : RecyclerView , hourView : RecyclerView, url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("ERROR","API ERROR")
            }
            override fun onResponse(call: Call, response: Response) {
                Log.d("DATA","chui là")
                val gson = Gson()
                val weatherData = gson.fromJson(response.body()?.string(), WeatherData::class.java )
                this@DetailedActivity.runOnUiThread {
                    Log.d("WEATHER", weatherData.toString())
                    updatingWeatherIc(weather_icon, weatherData.daily.weathercode[0])
                    updatingTempValue(
                        temperature_now_value,
                        weatherData.hourly.temperature_2m[findCurrentSlotHourly(weatherData)]

                    )
                    createHoursPrevision(weatherData, hourView)
                    createDayPrevision(weatherData, dayView)
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