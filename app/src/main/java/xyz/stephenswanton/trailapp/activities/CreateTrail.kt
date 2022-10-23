package xyz.stephenswanton.trailapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.databinding.ActivityCreateTrailBinding
import xyz.stephenswanton.trailapp.fragments.MarkerListFragment
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.Trail
import xyz.stephenswanton.trailapp.models.generateRandomId


class CreateTrail : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTrailBinding
    var app : MainApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTrailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        var markers = app!!.markers
        var markerListFragment = MarkerListFragment()
        var bundle = Bundle()
        bundle.putParcelableArrayList("markers", markers as ArrayList<out Parcelable?>?)
        markerListFragment.setArguments(bundle)



        supportFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, markerListFragment)
            commit()
        }
        var trail = Trail( generateRandomId(), "","" )
        binding.btnNewMarker
            .setOnClickListener{
                trail.name = binding.etTrailName.text.toString()
                trail.description = binding.etTrailDescription.text.toString()
                if (trail.name.isEmpty()) {
                    Snackbar.make(it,R.string.enter_trail_name, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                        app!!.tempTrail = trail.copy()
                        app!!.trails.create(trail.copy())
                        Intent(this, CreateMarker::class.java ).also{
                        startActivity(it)
                }

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
