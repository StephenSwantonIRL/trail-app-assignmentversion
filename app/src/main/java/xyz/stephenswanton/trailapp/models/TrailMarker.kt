package xyz.stephenswanton.trailapp.models

import android.R.attr.subtitle
import android.R.id
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize


@Parcelize
data class TrailMarker (
    var latitude: String,
    var longitude: String,
    var notes: String = "",
    ) : Parcelable