package xyz.stephenswanton.trailapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.stephenswanton.trailapp.databinding.ItemTrailBinding

class TrailAdapter constructor(
    var trails: List<Trail>
): RecyclerView.Adapter<TrailAdapter.TrailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {

        val binding = ItemTrailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TrailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrailAdapter.TrailViewHolder, position: Int) {
        val trail = trails[position]
        holder.bind(trail)
        }

    override fun getItemCount(): Int {
        return trails.size
    }

    class TrailViewHolder(private val binding : ItemTrailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(trail: Trail) {
            binding.tvTrailName.text = trail.name
        }
    }
}