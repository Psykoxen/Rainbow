package fr.rainbow.dataclasses

import android.R.id




data class Favorite(
    var name:String,
    var latitude:Double,
    var longitude:Double,
    val isGPS: Boolean,
    var isBig:Boolean,
    var isFavorite:Boolean,
    var weatherData: WeatherData?,
    var datetime: TimeAtLocation?
):java.io.Serializable
{


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Favorite)
            return false
        else {
            if (other.name == this.name && other.latitude == this.latitude && other.longitude == this.longitude && other.isGPS == this.isGPS)
                return true
        }
        return false
    }
}

