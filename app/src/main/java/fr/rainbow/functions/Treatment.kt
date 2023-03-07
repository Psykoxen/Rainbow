package fr.rainbow.functions

import WeatherData
import android.widget.ImageView
import android.widget.TextView
import fr.rainbow.R
import java.time.LocalDateTime

class Treatment {
    companion object {
        fun findCurrentSlotHourly(weatherData: WeatherData?): Int {
            val current = LocalDateTime.now()
            for (i in 0..weatherData!!.hourly.time.size) {
                if (weatherData.hourly.time[i] < current.toString()) {
                    if (weatherData.hourly.time[i+1] > current.toString()) {
                        return i
                    }
                }
            }
            return -1
        }

        fun updatingTempValue(temp: TextView, value: Any) {
            temp.text = value.toString()
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
}