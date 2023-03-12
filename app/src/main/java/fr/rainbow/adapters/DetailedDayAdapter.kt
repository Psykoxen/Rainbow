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
            updatingWeatherIc(itemView.weather_icon,dayWeather.weathercode)
            updatingTempValue(itemView.date_label,dayWeather.time)
            updatingTempValue(itemView.tmp_max_value,dayWeather.temperature_2m_max)
            if (dayWeather.temperature_2m_max < 0) {
                itemView.tmp_max_unit.setTextColor(Color.parseColor("#3F4DE1"))
                itemView.tmp_max_value.setTextColor(Color.parseColor("#3F4DE1"))
            } else if (dayWeather.temperature_2m_max > 40) {
                itemView.tmp_max_unit.setTextColor(Color.parseColor("E13F3F"))
                itemView.tmp_max_value.setTextColor(Color.parseColor("E13F3F"))
            }
            updatingTempValue(itemView.tmp_min_value,dayWeather.temperature_2m_min)
            if (dayWeather.temperature_2m_min < 0) {
                itemView.tmp_min_unit.setTextColor(Color.parseColor("#3F4DE1"))
                itemView.tmp_min_value.setTextColor(Color.parseColor("#3F4DE1"))
            } else if (dayWeather.temperature_2m_min > 40) {
                itemView.tmp_min_unit.setTextColor(Color.parseColor("E13F3F"))
                itemView.tmp_min_value.setTextColor(Color.parseColor("E13F3F"))
            }
            itemView.rain_probability.progress = dayWeather.precipitation_probability_max
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

        fun updatingTempValue(temp: TextView, value: Any) {
            temp.text = value.toString()
        }
    }


}
