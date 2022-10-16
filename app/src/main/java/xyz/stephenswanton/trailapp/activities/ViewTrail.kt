package xyz.stephenswanton.trailapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.databinding.ActivityViewTrailBinding
import xyz.stephenswanton.trailapp.fragments.MarkerListFragment
import xyz.stephenswanton.trailapp.fragments.TrailMapFragment

class ViewTrail : AppCompatActivity() {
    private lateinit var binding: ActivityViewTrailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTrailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var markerListFragment = MarkerListFragment()
        var trailMapFragment = TrailMapFragment()

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, markerListFragment)
            commit()
        }

        binding.btnMarkers.setOnClickListener{
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.flFragment, markerListFragment)
                commit()
            }
        }

        binding.btnMapView
            .setOnClickListener{
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.flFragment, trailMapFragment)
                commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.create_trail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miCancel -> finish();
        }
        return true
    }
}
