package xyz.stephenswanton.trailapp.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Trail(
    var id: Long = 0L,
    var name: String,
    var description: String? = null,
    var distance: Double? = null,
    var times: MutableList<TrailTime> = mutableListOf(),
    var markers: MutableList<TrailMarker> = mutableListOf()
) : Parcelable