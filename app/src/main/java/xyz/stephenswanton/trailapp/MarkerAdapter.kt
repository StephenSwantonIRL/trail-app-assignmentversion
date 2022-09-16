package xyz.stephenswanton.trailapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.stephenswanton.trailapp.databinding.ItemMarkerBinding

class MarkerAdapter constructor(
    var markers: List<TrailMarker>
): RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {

        val binding = ItemMarkerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MarkerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkerAdapter.MarkerViewHolder, position: Int) {
        val marker = markers[position]
        holder.bind(marker)
    }

    override fun getItemCount(): Int {
        return markers.size
    }

    class MarkerViewHolder(private val binding : ItemMarkerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(marker: TrailMarker) {
            binding.tvLatitude.text = "Latitude: "+ marker.latitude.toString()
            binding.tvLongitude.text = "Longitude: "+ marker.longitude.toString()
        }
    }
}