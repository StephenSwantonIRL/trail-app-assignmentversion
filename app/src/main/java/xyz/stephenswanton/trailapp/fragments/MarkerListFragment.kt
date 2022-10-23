package xyz.stephenswanton.trailapp.fragments
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.Marker
import xyz.stephenswanton.trailapp.adapters.MarkerAdapter
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.models.TrailMarker
import xyz.stephenswanton.trailapp.databinding.FragmentMarkerListBinding

class MarkerListFragment : Fragment(){
    private var adapter: RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>? = null
    private lateinit var binding: FragmentMarkerListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        binding = FragmentMarkerListBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        var markers: List<TrailMarker> = savedInstanceState?.getParcelable("markers") as? List<TrailMarker> ?: mutableListOf()

        val recyclerView = itemView.findViewById<RecyclerView>(R.id.rvMarkerList)
            recyclerView.layoutManager = LinearLayoutManager(context)
            // set the custom adapter to the RecyclerView

            adapter = MarkerAdapter(markers)
            recyclerView.adapter = adapter

    }
}