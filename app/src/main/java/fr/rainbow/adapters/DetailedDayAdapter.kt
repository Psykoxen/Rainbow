package fr.rainbow.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.rainbow.R
import fr.rainbow.dataclasses.DayWeatherData
import fr.rainbow.functions.Functions.dayUpdatingWeatherIc
import fr.rainbow.functions.Functions.getDayName
import fr.rainbow.functions.Functions.isTomorrow
import fr.rainbow.functions.Functions.updatingTempValue
import fr.rainbow.functions.Functions.updatingWeatherIc
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


            dayUpdatingWeatherIc(itemView.weather_icon, dayWeather.weathercode)
            if (isTomorrow(dayWeather.time)){
                itemView.date_label.text = "Tomorrow"
            } else {
                itemView.date_label.text = getDayName(dayWeather.time)
            }
            updatingTempValue(itemView.tmp_max_value, dayWeather.temperature_2m_max)
            if (dayWeather.temperature_2m_max < 0) {
                itemView.tmp_max_unit.setTextColor(Color.parseColor("#FF3D72B4"))
                itemView.tmp_max_value.setTextColor(Color.parseColor("#FF3D72B4"))
                itemView.tmp_max_unit.setTypeface(null, Typeface.BOLD)
                itemView.tmp_max_value.setTypeface(null, Typeface.BOLD)
            } else if (dayWeather.temperature_2m_max > 40) {
                itemView.tmp_max_unit.setTextColor(Color.parseColor("#FFE13F3F"))
                itemView.tmp_max_value.setTextColor(Color.parseColor("#FFE13F3F"))
                itemView.tmp_max_unit.setTypeface(null, Typeface.BOLD)
                itemView.tmp_max_value.setTypeface(null, Typeface.BOLD)
            }
            updatingTempValue(itemView.tmp_min_value, dayWeather.temperature_2m_min)
            if (dayWeather.temperature_2m_min < 0) {
                itemView.tmp_min_unit.setTextColor(Color.parseColor("#FF3D72B4"))
                itemView.tmp_min_value.setTextColor(Color.parseColor("#FF3D72B4"))
                itemView.tmp_min_unit.setTypeface(null, Typeface.BOLD)
                itemView.tmp_min_value.setTypeface(null, Typeface.BOLD)
            } else if (dayWeather.temperature_2m_min > 40) {
                itemView.tmp_min_unit.setTextColor(Color.parseColor("#FFE13F3F"))
                itemView.tmp_min_value.setTextColor(Color.parseColor("#FFE13F3F"))
                itemView.tmp_min_unit.setTypeface(null, Typeface.BOLD)
                itemView.tmp_min_value.setTypeface(null, Typeface.BOLD)
            }
            itemView.rain_probability.progress = dayWeather.precipitation_probability_max
        }

    }


}
