package fr.rainbow.ui.detailed


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import fr.rainbow.R
import fr.rainbow.adapters.DetailedDayAdapter
import fr.rainbow.adapters.DetailedHourlyAdapter
import fr.rainbow.dataclasses.WeatherData
import fr.rainbow.databinding.ActivityDetailedBinding
import fr.rainbow.dataclasses.DayWeatherData
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.dataclasses.HourWeatherData
import kotlinx.android.synthetic.main.activity_detailed.*
import okhttp3.*
import java.time.LocalDateTime



class DetailedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedBinding

    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private var name = "Your Position"
    private lateinit var favorite:Favorite

    private val hourPrevisionList : ArrayList<HourWeatherData> = ArrayList()
    private val dayPrevisionList: ArrayList<DayWeatherData> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_return.setOnClickListener {
            finish()
        }

        val recyclerDayView = dayView
        with(recyclerDayView) {
            layoutManager = LinearLayoutManager(this@DetailedActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter = DetailedDayAdapter(dayPrevisionList, context)
        }

        val recyclerHourView = hourView
        with(recyclerHourView) {
            layoutManager = LinearLayoutManager(this@DetailedActivity)
            adapter = DetailedHourlyAdapter(hourPrevisionList, context)
        }

        latitude = intent.getStringExtra("latitude")!!.toDouble()
        longitude = intent.getStringExtra("longitude")!!.toDouble()
        name = intent.getStringExtra("name")!!
        favorite = (intent.getSerializableExtra("favorite") as? Favorite)!!
        Log.d("test", favorite.toString())

        requestData(recyclerDayView,recyclerHourView, favorite.weatherData!!)
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

    private fun requestData(dayView: RecyclerView, hourView: RecyclerView, data: WeatherData) {
        updatingWeatherIc(weather_icon, data.daily.weathercode[0])
        updatingTempValue(
            temperature_now_value,
            data.hourly.temperature_2m[findCurrentSlotHourly(data)]

        )
        createHoursPrevision(data, hourView)
        createDayPrevision(data, dayView)
        cityName.text = name

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