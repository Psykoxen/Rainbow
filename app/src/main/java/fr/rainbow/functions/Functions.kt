package fr.rainbow.functions

import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.R
import fr.rainbow.dataclasses.WeatherData
import java.io.BufferedWriter
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


object Functions {

    private fun BufferedWriter.writeLn(line: String) {
        this.write(line)
        this.newLine()
    }

    fun writeFile(context:Context, favorites: ArrayList<Favorite>){
        Log.d("test"," write : ${favorites.size}")
        val path = context.filesDir
        val file = File(path,"somefile.txt")
        file.createNewFile()
        file.bufferedWriter().use { out ->
            favorites.forEach {
                out.writeLn("${it.name},${it.latitude},${it.longitude},${it.isGPS},${it.isBig},${it.isFavorite}")
            }
        }
    }

    fun readFile(context:Context): ArrayList<Favorite> {
        val path = context.filesDir
        val file = File(path,"somefile.txt")
        val favorites = ArrayList<Favorite>()
        file.createNewFile()
        val output = file.bufferedReader().use{it.readLines()}
        if(!output.isNullOrEmpty()){
            for(i in output){
                val string = i.split(",")
                val temp = Favorite(string[0],string[1].toDouble(),string[2].toDouble(),string[3].toBoolean(),string[4].toBoolean(),string[5].toBoolean(),null)
                favorites.add(temp)
            }
        }else{
            Log.e("fr.rainbow.favorite","nothing found")
        }
        return favorites
    }
    fun updatingBackgroundShape(layout : View, code: Int)
    {
        when (code) {
            1 -> layout.setBackgroundResource(R.drawable.shape_cloud_sun)
            2 -> layout.setBackgroundResource(R.drawable.shape_cloud)
            3 -> layout.setBackgroundResource(R.drawable.shape_cloud)
            45 -> layout.setBackgroundResource(R.drawable.shape_cloud)
            48 -> layout.setBackgroundResource(R.drawable.shape_cloud)
            51 -> layout.setBackgroundResource(R.drawable.shape_rain)
            53 -> layout.setBackgroundResource(R.drawable.shape_rain)
            55 -> layout.setBackgroundResource(R.drawable.shape_rain)
            56 -> layout.setBackgroundResource(R.drawable.shape_rain)
            57 -> layout.setBackgroundResource(R.drawable.shape_rain)
            61 -> layout.setBackgroundResource(R.drawable.shape_rain)
            63 -> layout.setBackgroundResource(R.drawable.shape_rain)
            65 -> layout.setBackgroundResource(R.drawable.shape_rain)
            66 -> layout.setBackgroundResource(R.drawable.shape_rain)
            67 -> layout.setBackgroundResource(R.drawable.shape_rain)
            71 -> layout.setBackgroundResource(R.drawable.shape_snow)
            73 -> layout.setBackgroundResource(R.drawable.shape_snow)
            75 -> layout.setBackgroundResource(R.drawable.shape_snow)
            77 -> layout.setBackgroundResource(R.drawable.shape_snow)
            80 -> layout.setBackgroundResource(R.drawable.shape_rain)
            81 -> layout.setBackgroundResource(R.drawable.shape_rain)
            82 -> layout.setBackgroundResource(R.drawable.shape_rain)
            95 -> layout.setBackgroundResource(R.drawable.shape_thunderstorm)
            96 -> layout.setBackgroundResource(R.drawable.shape_thunderstorm)
            99 -> layout.setBackgroundResource(R.drawable.shape_thunderstorm)
            else -> layout.setBackgroundResource(R.drawable.shape_main)

        }
    }

    fun updatingBackgroundWeatherColor(layout : View, code: Int)
    {
        when (code) {
            1 -> layout.setBackgroundColor(Color.parseColor("#AED6F1"))
            2 -> layout.setBackgroundColor(Color.parseColor("#BFC9CA"))
            3 -> layout.setBackgroundColor(Color.parseColor("#BFC9CA"))
            45 -> layout.setBackgroundColor(Color.parseColor("#BFC9CA"))
            48 -> layout.setBackgroundColor(Color.parseColor("#BFC9CA"))
            51 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            53 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            55 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            56 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            57 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            61 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            63 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            65 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            66 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            67 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            71 -> layout.setBackgroundColor(Color.parseColor("#85929E"))
            73 -> layout.setBackgroundColor(Color.parseColor("#85929E"))
            75 -> layout.setBackgroundColor(Color.parseColor("#85929E"))
            77 -> layout.setBackgroundColor(Color.parseColor("#85929E"))
            80 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            81 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            82 -> layout.setBackgroundColor(Color.parseColor("#1F618D"))
            95 -> layout.setBackgroundColor(Color.parseColor("#34495E"))
            96 -> layout.setBackgroundColor(Color.parseColor("#34495E"))
            99 -> layout.setBackgroundColor(Color.parseColor("#34495E"))
            else -> layout.setBackgroundColor(Color.parseColor("#BF4792FF"))
        }
    }
    fun updatingWeatherIc(icon: ImageView, code: Int) {
        when(code) {
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

    fun findCurrentSlotHourly(weatherData: WeatherData?): Int {
        if(weatherData!= null){
            val current = LocalDateTime.now()
            for (i in 0 until weatherData.hourly.time.size) {
                if (weatherData.hourly.time[i] < current.toString()) {
                    if (weatherData.hourly.time[i+1] > current.toString()) {
                        return i
                    }
                }
            }
        }
        return -1
    }

    fun getDayName(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dayFormat.format(date)
    }

    fun isTomorrow(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        return dateFormat.format(date) == dateFormat.format(tomorrow)
    }

}