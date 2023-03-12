package fr.rainbow.dataclasses

data class Place(
    val results: List<Result>,
)

data class Result(
    val geometry: Geometry
)

data class Geometry(
    val location: Location,
)

data class Location(
    val lat: Double,
    val lng: Double
)







