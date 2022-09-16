package xyz.stephenswanton.trailapp
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.stephenswanton.trailapp.databinding.FragmentMarkerListBinding

class MarkerListFragment : Fragment(){

    private var adapter: RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>? = null
    private lateinit var binding: FragmentMarkerListBinding

    val markers = mutableListOf(
        TrailMarker("123", "123"),
        TrailMarker("123", "123")
    )

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
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.rvMarkerList)
            recyclerView.layoutManager = LinearLayoutManager(context)
            // set the custom adapter to the RecyclerView
            adapter = MarkerAdapter(markers)
            recyclerView.adapter = adapter

    }
}