package fr.rainbow.dataclasses

data class MapsData(
    val hourly: Hourly2,
)

data class Hourly2(
    val time: List<String>,
    val weathercode: List<Int>
)