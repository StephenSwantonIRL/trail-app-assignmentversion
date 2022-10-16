package xyz.stephenswanton.trailapp.main

import android.app.Application
import xyz.stephenswanton.trailapp.models.Trail
import xyz.stephenswanton.trailapp.models.TrailMarker

class MainApp : Application() {
    var trails: MutableList<Trail> = mutableListOf(
        Trail("Trail 1")
    )
    var markers: MutableList<TrailMarker> = mutableListOf()
}