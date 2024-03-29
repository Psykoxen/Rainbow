package fr.rainbow.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.rainbow.R
import fr.rainbow.dataclasses.HourWeatherData
import fr.rainbow.functions.Functions.dayUpdatingWeatherIc
import fr.rainbow.functions.Functions.updatingTextValue
import kotlinx.android.synthetic.main.item_hour_weather.view.*


class DetailedHourlyAdapter(private val hourWeatherList: ArrayList<HourWeatherData>, private val context: Context)
    : RecyclerView.Adapter<DetailedHourlyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return(ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hour_weather, parent, false)
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hourWeather : HourWeatherData = hourWeatherList[position]

        holder.bind(hourWeather)

    }

    override fun getItemCount(): Int = hourWeatherList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hourWeather: HourWeatherData) {
            dayUpdatingWeatherIc(itemView.weather_icon,hourWeather.weathercode)
            updatingTextValue(itemView.time_label,hourWeather.dateTime)
            updatingTextValue(itemView.temperature_value,hourWeather.temperature_2m)
            if (hourWeather.temperature_2m < 0){
                    itemView.temperature_unit.setTextColor(itemView.context.getColor(R.color.cold_color))
                    itemView.temperature_value.setTextColor(itemView.context.getColor(R.color.cold_color))
                    itemView.temperature_unit.setTypeface(null, Typeface.BOLD)
                    itemView.temperature_value.setTypeface(null, Typeface.BOLD)
            } else if (hourWeather.temperature_2m > 40) {
                itemView.temperature_unit.setTextColor(itemView.context.getColor(R.color.hot_color))
                itemView.temperature_value.setTextColor(itemView.context.getColor(R.color.hot_color))
                itemView.temperature_unit.setTypeface(null, Typeface.BOLD)
                itemView.temperature_value.setTypeface(null, Typeface.BOLD)
            }
            if (hourWeather.snowfall > 0.0) {
                itemView.rain_icon.setImageResource(R.drawable.ic_snow)
                updatingTextValue(itemView.rain_value,hourWeather.snowfall)
            } else {
                updatingTextValue(itemView.rain_value,hourWeather.rain)
            }

            updatingTextValue(itemView.wind_value,hourWeather.windspeed_10m)
            itemView.wind_arrow.rotation = hourWeather.winddirection_10m.toFloat()
        }
    }


}
