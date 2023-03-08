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
            itemView.wind_arrow.setRotation(hourWeather.winddirection_10m.toFloat())
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
