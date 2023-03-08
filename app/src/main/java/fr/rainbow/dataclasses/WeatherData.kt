package fr.rainbow.dataclasses

data class WeatherData(
    val daily: Daily,
    val daily_units: DailyUnits,
    val elevation: Double,
    val generationtime_ms: Double,
    val hourly: Hourly,
    val hourly_units: HourlyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)

data class Daily(
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val uv_index_max: List<Double>,
    val weathercode: List<Int>
)

data class DailyUnits(
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String,
    val uv_index_max: String,
    val weathercode: String
)

data class Hourly(
    val apparent_temperature: List<Double>,
    val precipitation: List<Double>,
    val precipitation_probability: List<Int>,
    val rain: List<Double>,
    val relativehumidity_2m: List<Int>,
    val showers: List<Double>,
    val snowfall: List<Double>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weathercode: List<Int>,
    val winddirection_10m: List<Int>,
    val windspeed_10m: List<Double>
)

data class HourlyUnits(
    val apparent_temperature: String,
    val precipitation: String,
    val precipitation_probability: String,
    val rain: String,
    val relativehumidity_2m: String,
    val showers: String,
    val snowfall: String,
    val temperature_2m: String,
    val time: String,
    val weathercode: String,
    val winddirection_10m: String,
    val windspeed_10m: String
)
