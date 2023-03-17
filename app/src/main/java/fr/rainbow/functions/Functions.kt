package fr.rainbow.functions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.View
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import fr.rainbow.R
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.dataclasses.MapsData
import fr.rainbow.dataclasses.WeatherData
import java.io.BufferedWriter
import java.io.File
import java.time.LocalDateTime
import java.util.*


object Functions {

    private fun BufferedWriter.writeLn(line: String) {
        this.write(line)
        this.newLine()
    }

    fun writeFile(context:Context, favorites: ArrayList<Favorite>){
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
    fun updatingUvIc(icon: LottieAnimationView, value: Double?): Int {
        if (value == null) {
            icon.visibility = View.GONE
            return -1
        }
            if (value < 2) {
                icon.setAnimation(R.raw.uv_index_1)
            } else if ((value >= 2) && (value < 3)) {
                icon.setAnimation(R.raw.uv_index_2)
            } else if (value >= 3 && value < 4) {
                icon.setAnimation(R.raw.uv_index_3)
            } else if (value >= 4 && value < 5) {
                icon.setAnimation(R.raw.uv_index_4)
            } else if (value >= 5 && value < 6) {
                icon.setAnimation(R.raw.uv_index_5)
            } else if (value >= 6 && value < 7) {
                icon.setAnimation(R.raw.uv_index_6)
            } else if (value >= 7 && value < 8) {
                icon.setAnimation(R.raw.uv_index_7)
            } else if (value >= 8 && value < 9) {
                icon.setAnimation(R.raw.uv_index_8)
            } else if (value >= 9 && value < 10) {
                icon.setAnimation(R.raw.uv_index_9)
            } else if (value >= 10 && value < 11) {
                icon.setAnimation(R.raw.uv_index_10)
            } else {
                icon.setAnimation(R.raw.uv_index_11)
            }
        icon.playAnimation()
        return 0
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
    fun updatingBackgroundShapeColor(layout : View, code: Int)
    {
        when (code) {
            1 -> layout.setBackgroundResource(R.drawable.shape_cloud_sun_color)
            2 -> layout.setBackgroundResource(R.drawable.shape_cloud_color)
            3 -> layout.setBackgroundResource(R.drawable.shape_cloud_color)
            45 -> layout.setBackgroundResource(R.drawable.shape_cloud_color)
            48 -> layout.setBackgroundResource(R.drawable.shape_cloud_color)
            51 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            53 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            55 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            56 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            57 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            61 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            63 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            65 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            66 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            67 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            71 -> layout.setBackgroundResource(R.drawable.shape_snow_color)
            73 -> layout.setBackgroundResource(R.drawable.shape_snow_color)
            75 -> layout.setBackgroundResource(R.drawable.shape_snow_color)
            77 -> layout.setBackgroundResource(R.drawable.shape_snow_color)
            80 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            81 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            82 -> layout.setBackgroundResource(R.drawable.shape_rain_color)
            95 -> layout.setBackgroundResource(R.drawable.shape_thunderstorm_color)
            96 -> layout.setBackgroundResource(R.drawable.shape_thunderstorm_color)
            99 -> layout.setBackgroundResource(R.drawable.shape_thunderstorm_color)
            else -> layout.setBackgroundResource(R.drawable.shape_main_color)

        }
    }


    fun updatingWeatherIc(icon: LottieAnimationView, code: Int) {
        when(code) {
            0 -> icon.setAnimation(R.raw.clear_day)
            1 -> icon.setAnimation(R.raw.partly_cloudy_day)
            2 -> icon.setAnimation(R.raw.cloudy)
            3 -> icon.setAnimation(R.raw.extreme)
            45 -> icon.setAnimation(R.raw.fog)
            48 -> icon.setAnimation(R.raw.extreme_fog)
            51 -> icon.setAnimation(R.raw.partly_cloudy_day_drizzle)
            53 -> icon.setAnimation(R.raw.overcast_rain)
            55 -> icon.setAnimation(R.raw.overcast_rain)
            56 -> icon.setAnimation(R.raw.overcast_rain)
            57 -> icon.setAnimation(R.raw.overcast_rain)
            61 -> icon.setAnimation(R.raw.extreme_rain)
            63 -> icon.setAnimation(R.raw.extreme_rain)
            65 -> icon.setAnimation(R.raw.extreme_rain)
            66 -> icon.setAnimation(R.raw.extreme_rain)
            67 -> icon.setAnimation(R.raw.extreme_rain)
            71 -> icon.setAnimation(R.raw.partly_cloudy_day_snow)
            73 -> icon.setAnimation(R.raw.partly_cloudy_day_snow)
            75 -> icon.setAnimation(R.raw.partly_cloudy_day_snow)
            77 -> icon.setAnimation(R.raw.snow)
            80 -> icon.setAnimation(R.raw.extreme_rain)
            81 -> icon.setAnimation(R.raw.extreme_rain)
            82 -> icon.setAnimation(R.raw.extreme_rain)
            85 -> icon.setAnimation(R.raw.extreme_snow)
            86 -> icon.setAnimation(R.raw.extreme_snow)
            95 -> icon.setAnimation(R.raw.thunderstorms_extreme)
            96 -> icon.setAnimation(R.raw.thunderstorms_rain)
            99 -> icon.setAnimation(R.raw.thunderstorms_rain)
            else -> icon.setAnimation(R.raw.clear_day)
        }
        icon.playAnimation()
    }

    fun updatingWeatherBmpIc(icon: Marker?, context: Context, code: Int) {
        val bitmapSize = 200
        when(code) {
            0 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.clear_day_bmp, bitmapSize, bitmapSize)))
            1 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.partly_cloudy_day_bmp, bitmapSize, bitmapSize)))
            2 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.cloudy_bmp, bitmapSize, bitmapSize)))
            3 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_bmp, bitmapSize, bitmapSize)))
            45 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.fog_bmp, bitmapSize, bitmapSize)))
            48 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.fog_bmp, bitmapSize, bitmapSize)))
            51 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.partly_cloudy_day_drizzle_bmp, bitmapSize, bitmapSize)))
            53 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.overcast_rain_bmp, bitmapSize, bitmapSize)))
            55 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.overcast_rain_bmp, bitmapSize, bitmapSize)))
            56 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.overcast_rain_bmp, bitmapSize, bitmapSize)))
            57 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.overcast_rain_bmp, bitmapSize, bitmapSize)))
            61 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp, bitmapSize, bitmapSize)))
            63 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp, bitmapSize, bitmapSize)))
            65 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp, bitmapSize, bitmapSize)))
            66 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp, bitmapSize, bitmapSize)))
            67 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp, bitmapSize, bitmapSize)))
            71 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.partly_cloudy_day_snow_bmp, bitmapSize, bitmapSize)))
            73 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.partly_cloudy_day_snow_bmp, bitmapSize, bitmapSize)))
            75 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.partly_cloudy_day_snow_bmp, bitmapSize, bitmapSize)))
            77 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.snow_bmp, bitmapSize, bitmapSize)))
            80 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp, bitmapSize, bitmapSize)))
            81 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp, bitmapSize, bitmapSize)))
            82 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp, bitmapSize, bitmapSize)))
            85 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_snow_bmp, bitmapSize, bitmapSize)))
            86 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_snow_bmp, bitmapSize, bitmapSize)))
            95 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.thunderstorms_extreme_bmp, bitmapSize, bitmapSize)))
            96 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.thunderstorms_rain_bmp, bitmapSize, bitmapSize)))
            99 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.thunderstorms_rain_bmp, bitmapSize, bitmapSize)))
            else -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.clear_day_bmp, bitmapSize, bitmapSize)))
        }
    }

    private fun resizeMapIcons(context: Context, iconId: Int, width: Int, height: Int): Bitmap {
        val imageBitmap: Bitmap = BitmapFactory.decodeResource(
            context.resources,
            iconId
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
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

    fun findCurrentSlotHourly(weatherCode: MapsData): Int {
        if(weatherCode!= null){
            val current = LocalDateTime.now()
            for (i in 0 until weatherCode.hourly.time.size) {
                if (weatherCode.hourly.time[i] < current.toString()) {
                    if (weatherCode.hourly.time[i+1] > current.toString()) {
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