package xyz.stephenswanton.trailapp

data class Trail(
    var name: String,
    var description: String? = null,
    var distance: Double? = null,
    var times: MutableList<TrailTime> = mutableListOf(),
    var markers: MutableList<TrailMarker> = mutableListOf()

)