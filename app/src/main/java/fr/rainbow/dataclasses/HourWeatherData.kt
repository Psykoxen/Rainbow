package fr.rainbow.dataclasses

data class HourWeatherData(
    val dateTime: String,
    val apparent_temperature: Double,
    val precipitation: Double,
    val precipitation_probability: Int,
    val rain: Double,
    val relativehumidity_2m: Int,
    val showers: Double,
    val snowfall: Double,
    val temperature_2m: Double,
    val weathercode: Int,
    val winddirection_10m: Int,
    val windspeed_10m: Double,

)

