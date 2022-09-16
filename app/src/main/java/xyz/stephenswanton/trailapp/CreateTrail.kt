package xyz.stephenswanton.trailapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.stephenswanton.trailapp.databinding.ActivityCreateTrailBinding

class CreateTrail : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTrailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTrailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var markerListFragment = MarkerListFragment()
        var markerCreateFragment = MarkerCreateFragment()

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

        binding.btnNewMarker
            .setOnClickListener{
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.flFragment, markerCreateFragment)
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
