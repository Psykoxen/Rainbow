package fr.rainbow.dataclasses

data class DayWeatherData(
    val temperature_2m_max: Double,
    val temperature_2m_min: Double,
    val time: String,
    val uv_index_max: Double?,
    val weathercode: Int,
    val precipitation_probability_max: Int
)
