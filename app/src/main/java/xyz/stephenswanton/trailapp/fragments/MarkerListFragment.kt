package xyz.stephenswanton.trailapp.fragments
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber.i
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.activities.MainActivity
import xyz.stephenswanton.trailapp.activities.ViewTrail
import xyz.stephenswanton.trailapp.adapters.MarkerAdapter
import xyz.stephenswanton.trailapp.adapters.NavigateAction
import xyz.stephenswanton.trailapp.databinding.FragmentMarkerListBinding
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.TrailMarker


class MarkerListFragment : Fragment(), NavigateAction {
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
        var markers = arguments?.getParcelableArrayList<TrailMarker>("markers") as? List<TrailMarker> ?: listOf<TrailMarker>()
        i(markers.toString())
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.rvMarkerList)
            recyclerView.layoutManager = LinearLayoutManager(context)
            // set the custom adapter to the RecyclerView

            adapter = MarkerAdapter(markers, this)
            recyclerView.adapter = adapter

    }

    override fun onDeleteIconClick(marker: Long) {
        var app = activity?.application as MainApp?
        app!!.trails.deleteMarkerById(marker)
        var trailId = app!!.trails.idContainingMarker(marker)

        activity?.let{
            Intent( it, MainActivity::class.java ).apply{
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("trail_view", trailId)
            }.also{
                startActivity(it)
            }
        }
    }
}