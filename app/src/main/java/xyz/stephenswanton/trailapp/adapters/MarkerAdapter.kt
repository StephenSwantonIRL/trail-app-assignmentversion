package xyz.stephenswanton.trailapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.TrailListener
import xyz.stephenswanton.trailapp.activities.CreateMarker
import xyz.stephenswanton.trailapp.models.TrailMarker
import xyz.stephenswanton.trailapp.databinding.ItemMarkerBinding
import xyz.stephenswanton.trailapp.fragments.MarkerListFragment
import xyz.stephenswanton.trailapp.models.Trail

interface NavigateAction {
    fun onDeleteIconClick(marker: Long)
}

class MarkerAdapter(
    var markers: List<TrailMarker>,
    var listener: NavigateAction?
): RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {

        val binding = ItemMarkerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MarkerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val marker = markers[position]
        holder.bind(marker, listener)
    }

    override fun getItemCount(): Int {
        return markers.size
    }

    class MarkerViewHolder(private val binding : ItemMarkerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(marker: TrailMarker, listener: NavigateAction?) {
            binding.tvLatitude.text = "Latitude: "+ marker.latitude.toString()
            binding.tvLongitude.text = "Longitude: "+ marker.longitude.toString()
            binding.ivDelete.setOnClickListener{
                    listener?.onDeleteIconClick(marker.id)
                }
            }
        }
    }
