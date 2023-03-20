package fr.rainbow

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.rainbow.adapters.DetailedDayAdapter
import fr.rainbow.adapters.DetailedHourlyAdapter
import fr.rainbow.databinding.ActivityDetailedBinding
import fr.rainbow.dataclasses.DayWeatherData
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.dataclasses.HourWeatherData
import fr.rainbow.dataclasses.WeatherData
import fr.rainbow.functions.Functions.findCurrentSlotHourly
import fr.rainbow.functions.Functions.updatingBackgroundShapeColor
import fr.rainbow.functions.Functions.updatingTempValue
import fr.rainbow.functions.Functions.updatingUvIc
import fr.rainbow.functions.Functions.updatingWeatherIc
import kotlinx.android.synthetic.main.activity_detailed.*
import kotlinx.android.synthetic.main.item_hour_weather.view.*
import okhttp3.*


class DetailedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedBinding

    private lateinit var favorite:Favorite

    private val hourPrevisionList : ArrayList<HourWeatherData> = ArrayList()
    private val dayPrevisionList: ArrayList<DayWeatherData> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btn_return.setOnClickListener {
            returnMessage()
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

        favorite = (intent.getSerializableExtra("favorite") as? Favorite)!!

        requestData(recyclerDayView,recyclerHourView, favorite.weatherData!!)
        if(favorite.isFavorite){
            btn_favorite.setBackgroundResource(fr.rainbow.R.drawable.ic_star_full)
        }
        btn_favorite.setOnClickListener {
            if(favorite.isFavorite){
                favorite.isFavorite = false
                btn_favorite.setBackgroundResource(fr.rainbow.R.drawable.ic_star_empty)
            }else{
                favorite.isFavorite = true
                btn_favorite.setBackgroundResource(fr.rainbow.R.drawable.ic_star_full)
            }
        }
    }

    override fun onBackPressed() {
        returnMessage()
    }

    fun createHoursPrevision(weatherData: WeatherData, recyclerHourView: RecyclerView)
    {
        val index = findCurrentSlotHourly(weatherData)
        for (i in index..index+20)
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

        for (i in 3 until weatherData.daily.time.size)
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
        updatingBackgroundShapeColor(detailed_activity_layout,data.hourly.weathercode[findCurrentSlotHourly(data)],data.daily!!.sunset[2])
        if (updatingUvIc(uv_icon, data.daily.uv_index_max[1])!=0) {
            label_uv.visibility = View.GONE
            uv_icon.visibility = View.GONE
            barrierUV.visibility = View.GONE
        }
        updatingWeatherIc(weather_icon, data.hourly.weathercode[findCurrentSlotHourly(data)],data.daily!!.sunset[2])
        updatingTempValue(
            temperature_now_value,
            data.hourly.temperature_2m[findCurrentSlotHourly(data)]

        )
        if (data.hourly.temperature_2m[findCurrentSlotHourly(data)] < 0) {
            temperature_now_value.setTextColor(Color.parseColor("#FF3D72B4"))
            temperature_now_unit.setTextColor(Color.parseColor("#FF3D72B4"))
            temperature_now_unit.setTypeface(null, Typeface.BOLD)
            temperature_now_value.setTypeface(null, Typeface.BOLD)
        } else if (data.hourly.temperature_2m[findCurrentSlotHourly(data)] > 40) {
            temperature_now_value.setTextColor(Color.parseColor("#FFE13F3F"))
            temperature_now_unit.setTextColor(Color.parseColor("#FFE13F3F"))
            temperature_now_unit.setTypeface(null, Typeface.BOLD)
            temperature_now_value.setTypeface(null, Typeface.BOLD)
        }
        updatingTempValue(sunrise_value, data.daily!!.sunrise[0].substring(data.daily.sunrise[0].indexOf("T")+1,data.daily.sunrise[0].length))
        updatingTempValue(sunset_value, data.daily!!.sunset[0].substring(data.daily.sunset[0].indexOf("T")+1,data.daily.sunset[0].length))
        createHoursPrevision(data, hourView)
        createDayPrevision(data, dayView)
        cityName.text = favorite.name
    }

    fun returnMessage() {
        val returnIntent = intent.apply {
            putExtra("favorite",favorite)
        }
        setResult(RESULT_OK, returnIntent)
        finish()
    }

}