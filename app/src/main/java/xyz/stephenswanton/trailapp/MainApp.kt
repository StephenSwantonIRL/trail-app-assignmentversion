package xyz.stephenswanton.trailapp

import android.app.Application

class MainApp : Application() {
    var trails: MutableList<Trail> = mutableListOf(
        Trail("Trail 1")
    )
    var markers: MutableList<TrailMarker> = mutableListOf()
}