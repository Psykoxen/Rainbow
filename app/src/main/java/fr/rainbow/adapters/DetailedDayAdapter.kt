package fr.rainbow.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.rainbow.R
import fr.rainbow.dataclasses.DayWeatherData
import fr.rainbow.functions.Functions.updatingTempValue
import fr.rainbow.functions.Functions.updatingWeatherIc
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_day_weather.view.*
import kotlinx.android.synthetic.main.item_hour_weather.view.weather_icon


class DetailedDayAdapter(private val dayWeatherList: ArrayList<DayWeatherData>, private val context: Context)
    : RecyclerView.Adapter<DetailedDayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return(ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_day_weather, parent, false)
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayWeather : DayWeatherData = dayWeatherList[position]

        holder.bind(dayWeather)

    }

    override fun getItemCount(): Int = dayWeatherList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(dayWeather: DayWeatherData) {
            updatingWeatherIc(itemView.weather_icon, dayWeather.weathercode)
            updatingTempValue(itemView.date_label, dayWeather.time)
            updatingTempValue(itemView.tmp_max_value, dayWeather.temperature_2m_max)
            if (dayWeather.temperature_2m_max < 0) {
                itemView.tmp_max_unit.setTextColor(Color.parseColor("#3F4DE1"))
                itemView.tmp_max_value.setTextColor(Color.parseColor("#3F4DE1"))
            } else if (dayWeather.temperature_2m_max > 40) {
                itemView.tmp_max_unit.setTextColor(Color.parseColor("E13F3F"))
                itemView.tmp_max_value.setTextColor(Color.parseColor("E13F3F"))
            }
            updatingTempValue(itemView.tmp_min_value, dayWeather.temperature_2m_min)
            if (dayWeather.temperature_2m_min < 0) {
                itemView.tmp_min_unit.setTextColor(Color.parseColor("#3F4DE1"))
                itemView.tmp_min_value.setTextColor(Color.parseColor("#3F4DE1"))
            } else if (dayWeather.temperature_2m_min > 40) {
                itemView.tmp_min_unit.setTextColor(Color.parseColor("E13F3F"))
                itemView.tmp_min_value.setTextColor(Color.parseColor("E13F3F"))
            }
            itemView.rain_probability.progress = dayWeather.precipitation_probability_max
        }

    }


}
