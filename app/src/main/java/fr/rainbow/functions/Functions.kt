package fr.rainbow.functions

import android.content.Context
import android.graphics.Color
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


object Functions {

    private fun BufferedWriter.writeLn(line: String) {
        this.write(line)
        this.newLine()
    }

    fun writeFile(context:Context, Favorites: ArrayList<Favorite>){
        val path = context.filesDir
        val file = File(path,"somefile.txt")
        file.createNewFile()
        file.bufferedWriter().use { out ->
            Favorites.forEach {
                out.writeLn("${it.name},${it.latitude},${it.longitude},${it.isGPS},${it.isBig}")
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
                val temp = Favorite(string[0],string[1].toDouble(),string[2].toDouble(),string[3].toBoolean(),string[4].toBoolean(),null)
                favorites.add(temp)
            }
        }else{
            Log.d("fr.rainbow.favorite","nothing found")
        }
        return favorites
    }
    fun updatingBackgroundWeatherColor(layout : View, code: Int)
    {
        when (code) {
            1 -> layout.setBackgroundColor(Color.parseColor("#9CC0F4"))
            2 -> layout.setBackgroundColor(Color.parseColor("#D8D8D8"))
            3 -> layout.setBackgroundColor(Color.parseColor("#D8D8D8"))
            45 -> layout.setBackgroundColor(Color.parseColor("#D8D8D8"))
            48 -> layout.setBackgroundColor(Color.parseColor("#D8D8D8"))
            51 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            53 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            55 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            56 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            57 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            61 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            63 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            65 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            66 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            67 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            71 -> layout.setBackgroundColor(Color.parseColor("#CAC8EA"))
            73 -> layout.setBackgroundColor(Color.parseColor("#CAC8EA"))
            75 -> layout.setBackgroundColor(Color.parseColor("#CAC8EA"))
            77 -> layout.setBackgroundColor(Color.parseColor("#CAC8EA"))
            80 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            81 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            82 -> layout.setBackgroundColor(Color.parseColor("#407AE5"))
            95 -> layout.setBackgroundColor(Color.parseColor("#A7A7AD"))
            96 -> layout.setBackgroundColor(Color.parseColor("#818181"))
            99 -> layout.setBackgroundColor(Color.parseColor("#818181"))
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


}