package xyz.stephenswanton.trailapp.main

import android.app.Application
import xyz.stephenswanton.trailapp.models.TrailStore
import xyz.stephenswanton.trailapp.models.TrailJSONStore
import xyz.stephenswanton.trailapp.models.TrailMarker

class MainApp : Application() {
    lateinit var trails: TrailStore

    override fun onCreate() {
        super.onCreate()
        trails = TrailJSONStore(applicationContext)
    }

        var markers: MutableList<TrailMarker> = mutableListOf()
}