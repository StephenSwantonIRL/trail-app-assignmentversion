package xyz.stephenswanton.trailapp.main

import android.app.Application
import timber.log.Timber
import xyz.stephenswanton.trailapp.models.*

class MainApp : Application() {
    var markersArray: MutableList<TrailMarker> = mutableListOf()
    lateinit var trails: TrailStore
    lateinit var tempTrailObject: TrailStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        trails = TrailJSONStore(applicationContext)
        tempTrailObject = TempTrailJSONStore(applicationContext)
    }

    var markers: MutableList<TrailMarker> = mutableListOf()
    var tempTrail: Trail = Trail(0,"","")

    fun resetTempData(){
        tempTrailObject.deleteAll()
        markers = mutableListOf()
        tempTrail = Trail(0,"","")
        markersArray = mutableListOf()
    }


}