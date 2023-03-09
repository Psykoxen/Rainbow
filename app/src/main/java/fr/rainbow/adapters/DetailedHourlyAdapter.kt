package fr.rainbow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.rainbow.R
import fr.rainbow.dataclasses.HourWeatherData
import fr.rainbow.functions.file.updatingTempValue
import fr.rainbow.functions.file.updatingWeatherIc
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
            updatingWeatherIc(itemView.weather_icon,hourWeather.weathercode)
            updatingTempValue(itemView.time_label,hourWeather.dateTime)
            updatingTempValue(itemView.temperature_value,hourWeather.temperature_2m)
            updatingTempValue(itemView.wind_value,hourWeather.windspeed_10m)
            itemView.wind_arrow.rotation = hourWeather.winddirection_10m.toFloat()
        }
    }


}
