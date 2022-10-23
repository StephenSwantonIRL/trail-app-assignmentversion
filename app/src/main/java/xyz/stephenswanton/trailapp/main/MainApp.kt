package xyz.stephenswanton.trailapp.main

import android.app.Application
import timber.log.Timber
import xyz.stephenswanton.trailapp.models.Trail
import xyz.stephenswanton.trailapp.models.TrailStore
import xyz.stephenswanton.trailapp.models.TrailJSONStore
import xyz.stephenswanton.trailapp.models.TrailMarker

class MainApp : Application() {
    var markersArray: MutableList<TrailMarker> = mutableListOf()
    lateinit var trails: TrailStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        trails = TrailJSONStore(applicationContext)
    }

    var markers: MutableList<TrailMarker> = mutableListOf()
    var tempTrail: Trail = Trail(0,"","")

}