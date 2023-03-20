package fr.rainbow.functions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import fr.rainbow.R
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.dataclasses.MapsData
import java.io.BufferedWriter
import java.io.File
import java.time.LocalDateTime
import java.util.*


object Functions {

    /** Cette fonction permet d'écire dans un fichier texte pour sauvegarder les favoris.
     * @param line Un objet de type [String] qui représente la ligne à écrire.
     */
    private fun BufferedWriter.writeLn(line: String) {
        this.write(line)
        this.newLine()
    }

    /**
     * Cette fonction permet d'écrire dans un fichier texte pour sauvegarder les favoris.
     * @param context Un objet de type [Context] utilisé pour avoir le contexte de l'application au sein du téléphone.
     * @param favorites Un objet de type [ArrayList] contenant les favoris.
     */
    fun writeFile(context:Context, favorites: ArrayList<Favorite>)
    {
        val path = context.filesDir
        val file = File(path,"saveFavorite.txt")
        file.createNewFile()
        file.bufferedWriter().use { out ->
            favorites.forEach {
                out.writeLn("${it.name},${it.latitude},${it.longitude},${it.isGPS},${it.isBig},${it.isFavorite}")
            }
        }
    }

    /**
     * Cette fonction permet de lire un fichier texte pour récupérer les favoris.
     * @param context Un objet de type [Context] utilisé pour avoir le contexte de l'application au sein du téléphone.
     * @return Un objet de type [ArrayList] contenant les favoris.
     */
    fun readFile(context:Context): ArrayList<Favorite>
    {
        val path = context.filesDir
        val file = File(path,"saveFavorite.txt")
        val favorites = ArrayList<Favorite>()
        file.createNewFile()
        val output = file.bufferedReader().use{it.readLines()}
        if(output.isNotEmpty()){
            for(i in output){
                val string = i.split(",")
                val temp = Favorite(string[0],string[1].toDouble(),string[2].toDouble(),string[3].toBoolean(),string[4].toBoolean(),string[5].toBoolean(),null,null)
                favorites.add(temp)
            }
        }else{
            Log.e("fr.rainbow.favorite","nothing found")
        }
        return favorites
    }

    /**
     * Cette fonction permet de modifier le LottieAnimationView dédié à l'indice UV.
     * @param icon Un objet de type [LottieAnimationView] qui recevra l'indice UV.
     * @param value Un objet de type [Double] qui représente l'indice UV.
     * @return Un [Int] de 0 si tout a fonctioné, -1 sinon.
     */
    fun updatingUvIc(icon: LottieAnimationView, value: Double?): Int
    {
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

    /**
     * Cette fonction permet de modifier le background de l'activité détaillé suivant les conditions météo et l'heure.
     * @param layout Un objet de type [View] qui recevra le background.
     * @param code Un objet de type [Int] qui représente le code météo.
     * @param sunset Un objet de type [String] qui représente l'heure du coucher du soleil.
     * @param sunrise Un objet de type [String] qui représente l'heure du lever du soleil.
     * @param datetime_now Un objet de type [String] qui représente l'heure actuelle.
     */
    fun updatingBackgroundShape(layout : View, code: Int,sunset: String,sunrise: String,datetime_now: String)
    {
        val now = LocalDateTime.of(datetime_now.substring(0,4).toInt(), datetime_now.substring(5,7).toInt(), datetime_now.substring(8,10).toInt(), datetime_now.substring(11,13).toInt(),datetime_now.substring(14,16).toInt())
        val sunset = LocalDateTime.of(sunset.substring(0,4).toInt(), sunset.substring(5,7).toInt(), sunset.substring(8,10).toInt(), sunset.substring(11,13).toInt(),sunset.substring(14,16).toInt())
        val sunrise = LocalDateTime.of(sunrise.substring(0,4).toInt(), sunrise.substring(5,7).toInt(), sunrise.substring(8,10).toInt(), sunrise.substring(11,13).toInt(),sunrise.substring(14,16).toInt())
        if (now.compareTo(sunset) > 0 || now.compareTo(sunrise) < 0) {
            nightUpdatingBackgroundShape(layout, code)
        } else {
            dayUpdatingBackgroundShape(layout, code)
        }
    }

    /**
     * Cette fonction permet de modifier le background des favoris durant le jour.
     * @param layout Un objet de type [View] qui recevra le background.
     * @param code Un objet de type [Int] qui représente le code météo.
     */
    private fun dayUpdatingBackgroundShape(layout : View, code: Int)
    {
        when (code) {
            1 -> layout.setBackgroundResource(R.drawable.shape_day_cloud_sun)
            2 -> layout.setBackgroundResource(R.drawable.shape_day_cloud)
            3 -> layout.setBackgroundResource(R.drawable.shape_day_cloud)
            45 -> layout.setBackgroundResource(R.drawable.shape_day_cloud)
            48 -> layout.setBackgroundResource(R.drawable.shape_day_cloud)
            51 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            53 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            55 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            56 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            57 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            61 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            63 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            65 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            66 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            67 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            71 -> layout.setBackgroundResource(R.drawable.shape_day_snow)
            73 -> layout.setBackgroundResource(R.drawable.shape_day_snow)
            75 -> layout.setBackgroundResource(R.drawable.shape_day_snow)
            77 -> layout.setBackgroundResource(R.drawable.shape_day_snow)
            80 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            81 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            82 -> layout.setBackgroundResource(R.drawable.shape_day_rain)
            85 -> layout.setBackgroundResource(R.drawable.shape_day_snow)
            86 -> layout.setBackgroundResource(R.drawable.shape_day_snow)
            95 -> layout.setBackgroundResource(R.drawable.shape_day_thunderstorm)
            96 -> layout.setBackgroundResource(R.drawable.shape_day_thunderstorm)
            99 -> layout.setBackgroundResource(R.drawable.shape_day_thunderstorm)
            else -> layout.setBackgroundResource(R.drawable.shape_day_main)

        }
    }

    /**
     * Cette fonction permet de modifier le background des favoris durant la nuit.
     * @param layout Un objet de type [View] qui recevra le background.
     * @param code Un objet de type [Int] qui représente le code météo.
     */
    private fun nightUpdatingBackgroundShape(layout : View, code: Int)
    {
        when (code) {
            1 -> layout.setBackgroundResource(R.drawable.shape_night_cloud_sun)
            2 -> layout.setBackgroundResource(R.drawable.shape_night_cloud)
            3 -> layout.setBackgroundResource(R.drawable.shape_night_cloud)
            45 -> layout.setBackgroundResource(R.drawable.shape_night_cloud)
            48 -> layout.setBackgroundResource(R.drawable.shape_night_cloud)
            51 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            53 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            55 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            56 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            57 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            61 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            63 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            65 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            66 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            67 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            71 -> layout.setBackgroundResource(R.drawable.shape_night_snow)
            73 -> layout.setBackgroundResource(R.drawable.shape_night_snow)
            75 -> layout.setBackgroundResource(R.drawable.shape_night_snow)
            77 -> layout.setBackgroundResource(R.drawable.shape_night_snow)
            80 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            81 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            82 -> layout.setBackgroundResource(R.drawable.shape_night_rain)
            85 -> layout.setBackgroundResource(R.drawable.shape_night_snow)
            86 -> layout.setBackgroundResource(R.drawable.shape_night_snow)
            95 -> layout.setBackgroundResource(R.drawable.shape_night_thunderstorm)
            96 -> layout.setBackgroundResource(R.drawable.shape_night_thunderstorm)
            99 -> layout.setBackgroundResource(R.drawable.shape_night_thunderstorm)
            else -> layout.setBackgroundResource(R.drawable.shape_night_main)

        }
    }

    /**
     * Cette fonction permet de modifier la couleur de background de l'activité détaillé suivant les conditions météo et l'heure.
     * @param layout Un objet de type [View] qui recevra le background.
     * @param code Un objet de type [Int] qui représente le code météo.
     * @param sunset Un objet de type [String] qui représente l'heure du coucher du soleil.
     * @param sunrise Un objet de type [String] qui représente l'heure du lever du soleil.
     * @param datetime_now Un objet de type [String] qui représente l'heure actuelle.
     */
    fun updatingBackgroundShapeColor(
        layout: View,
        code: Int,
        sunset: String,
        sunrise: String,
        datetime_now: String
    ) {
        val now = LocalDateTime.of(datetime_now.substring(0,4).toInt(), datetime_now.substring(5,7).toInt(), datetime_now.substring(8,10).toInt(), datetime_now.substring(11,13).toInt(),datetime_now.substring(14,16).toInt())
        val sunset = LocalDateTime.of(sunset.substring(0,4).toInt(), sunset.substring(5,7).toInt(), sunset.substring(8,10).toInt(), sunset.substring(11,13).toInt(),sunset.substring(14,16).toInt())
        val sunrise = LocalDateTime.of(sunrise.substring(0,4).toInt(), sunrise.substring(5,7).toInt(), sunrise.substring(8,10).toInt(), sunrise.substring(11,13).toInt(),sunrise.substring(14,16).toInt())
        if (now.compareTo(sunset) > 0 || now.compareTo(sunrise) < 0) {
            nightUpdatingBackgroundShapeColor(layout, code)
        } else {
            dayUpdatingBackgroundShapeColor(layout, code)
        }
    }

    /**
     * Cette fonction permet de modifier la couleur de background des favoris durant le jour.
     * @param layout Un objet de type [View] qui recevra le background.
     * @param code Un objet de type [Int] qui représente le code météo.
     */
    private fun dayUpdatingBackgroundShapeColor(layout : View, code: Int)
    {
        when (code) {
            1 -> layout.setBackgroundResource(R.drawable.shape_day_cloud_sun_color)
            2 -> layout.setBackgroundResource(R.drawable.shape_day_cloud_color)
            3 -> layout.setBackgroundResource(R.drawable.shape_day_cloud_color)
            45 -> layout.setBackgroundResource(R.drawable.shape_day_cloud_color)
            48 -> layout.setBackgroundResource(R.drawable.shape_day_cloud_color)
            51 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            53 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            55 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            56 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            57 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            61 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            63 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            65 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            66 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            67 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            71 -> layout.setBackgroundResource(R.drawable.shape_day_snow_color)
            73 -> layout.setBackgroundResource(R.drawable.shape_day_snow_color)
            75 -> layout.setBackgroundResource(R.drawable.shape_day_snow_color)
            77 -> layout.setBackgroundResource(R.drawable.shape_day_snow_color)
            80 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            81 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            82 -> layout.setBackgroundResource(R.drawable.shape_day_rain_color)
            85 -> layout.setBackgroundResource(R.drawable.shape_day_snow_color)
            86 -> layout.setBackgroundResource(R.drawable.shape_day_snow_color)
            95 -> layout.setBackgroundResource(R.drawable.shape_day_thunderstorm_color)
            96 -> layout.setBackgroundResource(R.drawable.shape_day_thunderstorm_color)
            99 -> layout.setBackgroundResource(R.drawable.shape_day_thunderstorm_color)
            else -> layout.setBackgroundResource(R.drawable.shape_day_main_color)

        }
    }

    /**
     * Cette fonction permet de modifier la couleur de background des favoris durant la nuit.
     * @param layout Un objet de type [View] qui recevra le background.
     * @param code Un objet de type [Int] qui représente le code météo.
     */
    private fun nightUpdatingBackgroundShapeColor(layout : View, code: Int)
    {
        when (code) {
            1 -> layout.setBackgroundResource(R.drawable.shape_night_cloud_sun_color)
            2 -> layout.setBackgroundResource(R.drawable.shape_night_cloud_color)
            3 -> layout.setBackgroundResource(R.drawable.shape_night_cloud_color)
            45 -> layout.setBackgroundResource(R.drawable.shape_night_cloud_color)
            48 -> layout.setBackgroundResource(R.drawable.shape_night_cloud_color)
            51 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            53 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            55 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            56 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            57 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            61 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            63 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            65 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            66 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            67 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            71 -> layout.setBackgroundResource(R.drawable.shape_night_snow_color)
            73 -> layout.setBackgroundResource(R.drawable.shape_night_snow_color)
            75 -> layout.setBackgroundResource(R.drawable.shape_night_snow_color)
            77 -> layout.setBackgroundResource(R.drawable.shape_night_snow_color)
            80 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            81 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            82 -> layout.setBackgroundResource(R.drawable.shape_night_rain_color)
            85 -> layout.setBackgroundResource(R.drawable.shape_night_snow_color)
            86 -> layout.setBackgroundResource(R.drawable.shape_night_snow_color)
            95 -> layout.setBackgroundResource(R.drawable.shape_night_thunderstorm_color)
            96 -> layout.setBackgroundResource(R.drawable.shape_night_thunderstorm_color)
            99 -> layout.setBackgroundResource(R.drawable.shape_night_thunderstorm_color)
            else -> layout.setBackgroundResource(R.drawable.shape_night_main_color)

        }
    }

    /**
     * Cette fonction permet de modifier l'icône de météo suivant les conditions météo et l'heure.
     * @param icon Un objet de type [LottieAnimationView] qui recevra l'icône.
     * @param code Un objet de type [Int] qui représente le code météo.
     * @param sunset Un objet de type [String] qui représente l'heure du coucher du soleil.
     * @param sunrise Un objet de type [String] qui représente l'heure du lever du soleil.
     * @param datetime_now Un objet de type [String] qui représente l'heure actuelle.
     */
    fun updatingWeatherIc(icon: LottieAnimationView, code: Int, sunset: String,sunrise: String, datetime_now: String)
    {
        val now = LocalDateTime.of(datetime_now.substring(0,4).toInt(), datetime_now.substring(5,7).toInt(), datetime_now.substring(8,10).toInt(), datetime_now.substring(11,13).toInt(),datetime_now.substring(14,16).toInt())
        val sunset = LocalDateTime.of(sunset.substring(0,4).toInt(), sunset.substring(5,7).toInt(), sunset.substring(8,10).toInt(), sunset.substring(11,13).toInt(),sunset.substring(14,16).toInt())
        val sunrise = LocalDateTime.of(sunrise.substring(0,4).toInt(), sunrise.substring(5,7).toInt(), sunrise.substring(8,10).toInt(), sunrise.substring(11,13).toInt(),sunrise.substring(14,16).toInt())
        if (now.compareTo(sunset) > 0 || now.compareTo(sunrise) < 0) {
            nightUpdatingWeatherIc(icon, code)
        } else {
            dayUpdatingWeatherIc(icon, code)
        }
    }

    /**
     * Cette fonction permet de modifier l'icône de météo durant le jour.
     * @param icon Un objet de type [LottieAnimationView] qui recevra l'icône.
     * @param code Un objet de type [Int] qui représente le code météo.
     */
    fun dayUpdatingWeatherIc(icon: LottieAnimationView, code: Int) {
        when(code) {
            0 -> icon.setAnimation(R.raw.clear_day)
            1 -> icon.setAnimation(R.raw.partly_cloudy_day)
            2 -> icon.setAnimation(R.raw.cloudy)
            3 -> icon.setAnimation(R.raw.extreme_day)
            45 -> icon.setAnimation(R.raw.fog_day)
            48 -> icon.setAnimation(R.raw.extreme_day_fog)
            51 -> icon.setAnimation(R.raw.partly_cloudy_day_drizzle)
            53 -> icon.setAnimation(R.raw.overcast_day_rain)
            55 -> icon.setAnimation(R.raw.overcast_day_rain)
            56 -> icon.setAnimation(R.raw.overcast_day_rain)
            57 -> icon.setAnimation(R.raw.overcast_day_rain)
            61 -> icon.setAnimation(R.raw.overcast_day_rain)
            63 -> icon.setAnimation(R.raw.overcast_day_snow)
            65 -> icon.setAnimation(R.raw.overcast_day_rain)
            66 -> icon.setAnimation(R.raw.overcast_day_rain)
            67 -> icon.setAnimation(R.raw.overcast_day_rain)
            71 -> icon.setAnimation(R.raw.overcast_day_snow)
            73 -> icon.setAnimation(R.raw.overcast_day_snow)
            75 -> icon.setAnimation(R.raw.overcast_day_snow)
            77 -> icon.setAnimation(R.raw.overcast_day_snow)
            80 -> icon.setAnimation(R.raw.overcast_day_rain)
            81 -> icon.setAnimation(R.raw.overcast_day_rain)
            82 -> icon.setAnimation(R.raw.overcast_day_rain)
            85 -> icon.setAnimation(R.raw.overcast_day_snow)
            86 -> icon.setAnimation(R.raw.overcast_day_snow)
            95 -> icon.setAnimation(R.raw.thunderstorms_day_extreme)
            96 -> icon.setAnimation(R.raw.thunderstorms_day_rain)
            99 -> icon.setAnimation(R.raw.thunderstorms_day_rain)
            else -> icon.setAnimation(R.raw.clear_day)
        }
        icon.playAnimation()
    }

    /**
     * Cette fonction permet de modifier l'icône de météo durant la nuit.
     * @param icon Un objet de type [LottieAnimationView] qui recevra l'icône.
     * @param code Un objet de type [Int] qui représente le code météo.
     */
    private fun nightUpdatingWeatherIc(icon: LottieAnimationView, code: Int) {
        when(code) {
            0 -> icon.setAnimation(R.raw.clear_night)
            1 -> icon.setAnimation(R.raw.partly_cloudy_night)
            2 -> icon.setAnimation(R.raw.cloudy)
            3 -> icon.setAnimation(R.raw.extreme_night)
            45 -> icon.setAnimation(R.raw.fog_night)
            48 -> icon.setAnimation(R.raw.extreme_night_fog)
            51 -> icon.setAnimation(R.raw.partly_cloudy_night_drizzle)
            53 -> icon.setAnimation(R.raw.overcast_night_rain)
            55 -> icon.setAnimation(R.raw.overcast_night_rain)
            56 -> icon.setAnimation(R.raw.overcast_night_rain)
            57 -> icon.setAnimation(R.raw.overcast_night_rain)
            61 -> icon.setAnimation(R.raw.overcast_night_rain)
            63 -> icon.setAnimation(R.raw.overcast_night_snow)
            65 -> icon.setAnimation(R.raw.overcast_night_rain)
            66 -> icon.setAnimation(R.raw.overcast_night_rain)
            67 -> icon.setAnimation(R.raw.overcast_night_rain)
            71 -> icon.setAnimation(R.raw.overcast_night_snow)
            73 -> icon.setAnimation(R.raw.overcast_night_snow)
            75 -> icon.setAnimation(R.raw.overcast_night_snow)
            77 -> icon.setAnimation(R.raw.overcast_night_snow)
            80 -> icon.setAnimation(R.raw.overcast_night_rain)
            81 -> icon.setAnimation(R.raw.overcast_night_rain)
            82 -> icon.setAnimation(R.raw.overcast_night_rain)
            85 -> icon.setAnimation(R.raw.overcast_night_snow)
            86 -> icon.setAnimation(R.raw.overcast_night_snow)
            95 -> icon.setAnimation(R.raw.thunderstorms_night_extreme)
            96 -> icon.setAnimation(R.raw.thunderstorms_night_rain)
            99 -> icon.setAnimation(R.raw.thunderstorms_night_rain)
            else -> icon.setAnimation(R.raw.clear_night)
        }
        icon.playAnimation()
    }

    /**
     * Cette fonction permet de modifier l'icône de météo du widget.
     * @param icon Un objet de type [RemoteViews] qui recevra l'icône.
     * @param code Un objet de type [Int] qui représente le code météo.
     */
    fun updatingWeatherWidgetIc(icon: RemoteViews, code: Int) {
        when (code) {
            0 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_clear_day)
            1 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_partly_cloudy_day)
            2 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_cloudy)
            3 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme)
            45 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_fog)
            48 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_fog)
            51 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_partly_cloudy_day_drizzle)
            53 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_overcast_rain)
            55 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_overcast_rain)
            56 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_overcast_rain)
            57 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_overcast_rain)
            61 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme_rain)
            63 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme_rain)
            65 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme_rain)
            66 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme_rain)
            67 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme_rain)
            71 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_partly_cloudy_day_snow)
            73 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_partly_cloudy_day_snow)
            75 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_partly_cloudy_day_snow)
            77 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_snow)
            80 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme_rain)
            81 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme_rain)
            82 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme_rain)
            85 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme_rain)
            86 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_extreme_rain)
            95 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_thunderstorms_extreme)
            96 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_thunderstorms_rain)
            99 -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_thunderstorms_rain)
            else -> icon.setImageViewResource(R.id.weather_icon,R.drawable.weather_ic_clear_day)
        }
    }

    /**
     * Cette fonction permet de modifier l'icône de météo de la carte.
     * @param icon Un objet de type [Marker] qui recevra l'icône.
     * @param context Un objet de type [Context] qui représente le contexte de l'application.
     * @param code Un objet de type [Int] qui représente le code météo.
     */
    fun updatingWeatherBmpIc(icon: Marker?, context: Context, code: Int) {
        when(code) {
            0 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.clear_day_bmp)))
            1 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.partly_cloudy_day_bmp)))
            2 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.cloudy_bmp)))
            3 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_bmp)))
            45 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.fog_bmp)))
            48 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.fog_bmp)))
            51 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.partly_cloudy_day_drizzle_bmp)))
            53 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.overcast_rain_bmp)))
            55 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.overcast_rain_bmp)))
            56 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.overcast_rain_bmp)))
            57 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.overcast_rain_bmp)))
            61 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp)))
            63 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp)))
            65 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp)))
            66 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp)))
            67 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp)))
            71 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.partly_cloudy_day_snow_bmp)))
            73 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.partly_cloudy_day_snow_bmp)))
            75 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.partly_cloudy_day_snow_bmp)))
            77 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.snow_bmp)))
            80 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp)))
            81 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp)))
            82 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_rain_bmp)))
            85 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_snow_bmp)))
            86 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.extreme_snow_bmp)))
            95 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.thunderstorms_extreme_bmp)))
            96 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.thunderstorms_rain_bmp)))
            99 -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.thunderstorms_rain_bmp)))
            else -> icon?.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(context, R.raw.clear_day_bmp)))
        }
    }

    /**
     * Cette fonction permet de redimensionner les icônes de météo de la carte.
     * @param context Un objet de type [Context] qui représente le contexte de l'application.
     * @param iconId Un objet de type [Int] qui représente l'identifiant de l'icône.
     * @return Un objet de type [Bitmap] qui représente l'icône redimensionnée.
     */
    private fun resizeMapIcons(context: Context, iconId: Int): Bitmap {
        val imageBitmap: Bitmap = BitmapFactory.decodeResource(
            context.resources,
            iconId
        )
        return Bitmap.createScaledBitmap(imageBitmap, 75, 100, false)
    }

    /**
     * Cette fonction permet de modifier la valeur d'une TextView.
     * @param temp Un objet de type [TextView] qui recevra la valeur.
     * @param value Un objet de type [Any] qui représente la valeur.
     */
    fun updatingTextValue(temp: TextView, value: Any) {
        temp.text = value.toString()
    }

    /**
     * Cette fonction permet de trouver l'index de l'heure actuelle dans la liste des heures.
     * @param data Un objet de type [Favorite] qui représente les données météo d'un favori.
     * @return Un objet de type [Int] qui représente l'index de l'heure actuelle.
     */
    fun findCurrentSlotHourly(data: Favorite): Int {
            val current = LocalDateTime.now()

                for (i in 0 until data.weatherData!!.hourly.time.size) {
                if (data.weatherData!!.hourly.time[i] < current.toString()) {
                    if (data.weatherData!!.hourly.time[i+1] > current.toString()) {
                        return i
                    }
                }
            }

        return -1
    }

    /**
     * Cette fonction permet de trouver l'index de l'heure actuelle dans la liste des heures.
     * @param weatherCode Un objet de type [MapsData] qui représente les données météo d'une ville.
     * @return Un objet de type [Int] qui représente l'index de l'heure actuelle.
     */
    fun findCurrentSlotHourly(weatherCode: MapsData): Int {
        val current = LocalDateTime.now()
        for (i in 0 until weatherCode.hourly.time.size) {
            if (weatherCode.hourly.time[i] < current.toString()) {
                if (weatherCode.hourly.time[i+1] > current.toString()) {
                    return i
                }
            }
        }
        return -1
    }

    /**
     * Cette fonction permet de récupérer le nom du jour à partir d'une date.
     * @param dateString Un objet de type [String] qui représente la date.
     * @return Un objet de type [String] qui représente le nom du jour.
     */
    fun getDayName(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dayFormat.format(date)
    }

    /**
     * Cette fonction permet de savoir si une date est celle de demain.
     * @param dateString Un objet de type [String] qui représente la date.
     * @return Un objet de type [Boolean] qui représente si la date est celle de demain.
     */
    fun isTomorrow(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        return dateFormat.format(date) == dateFormat.format(tomorrow)
    }

}