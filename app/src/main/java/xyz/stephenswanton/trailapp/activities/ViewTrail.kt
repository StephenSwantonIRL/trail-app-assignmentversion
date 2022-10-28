package xyz.stephenswanton.trailapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import timber.log.Timber
import timber.log.Timber.i
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.databinding.ActivityViewTrailBinding
import xyz.stephenswanton.trailapp.fragments.MarkerListFragment
import xyz.stephenswanton.trailapp.fragments.TrailMapFragment
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.Trail
import xyz.stephenswanton.trailapp.models.TrailMarker
import xyz.stephenswanton.trailapp.models.generateRandomId

class ViewTrail : AppCompatActivity() {
    private lateinit var binding: ActivityViewTrailBinding
    var app: MainApp? = null
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTrailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        var edit = false
        var trail = Trail(generateRandomId(), "", "")
        if (intent.hasExtra("trail_view")) {

            edit = true
            trail = intent.extras?.getParcelable<Trail>("trail_view")!!
            app!!.tempTrail = app!!.trails.findById(trail.id) ?: trail
            binding.tvTrailName.setText(trail.name)
            binding.tvTrailDescription.setText(trail.description)
        }

        var markers = trail.markers
        var markerListFragment = MarkerListFragment()
        var bundle = Bundle()
        bundle.putParcelableArrayList("markers", markers as ArrayList<out Parcelable?>?)
        markerListFragment.setArguments(bundle)

        var trailMapFragment = TrailMapFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, markerListFragment)
            commit()
        }

        binding.btnMarkers.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, markerListFragment)
                commit()
            }
        }

        binding.btnMapView
            .setOnClickListener {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, trailMapFragment)
                    commit()
                }
            }
        fun onDeleteIconClick(marker: TrailMarker) {
            app!!.trails.deleteMarkerById(marker.id)
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, markerListFragment)
                commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.create_trail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miSave -> {

                app!!.trails.update(app!!.tempTrail)
                app!!.resetTempData()
                finish()
            };

            R.id.miCancel -> {
                app!!.resetTempData()
                finish()
            };
        }
        return true
    }


}


