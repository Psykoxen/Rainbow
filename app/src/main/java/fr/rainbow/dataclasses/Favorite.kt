package fr.rainbow.dataclasses

data class Favorite(
    var name:String,
    var latitude:Double,
    var longitude:Double,
    val isGPS: Boolean,
    var isBig:Boolean,
    var weatherData: WeatherData?):java.io.Serializable{

}


