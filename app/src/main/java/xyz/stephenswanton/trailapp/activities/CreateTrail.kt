package xyz.stephenswanton.trailapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.databinding.ActivityCreateTrailBinding
import xyz.stephenswanton.trailapp.fragments.MarkerListFragment
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.Trail
import xyz.stephenswanton.trailapp.models.generateRandomId
import timber.log.Timber
import timber.log.Timber.i
import androidx.activity.result.contract.ActivityResultContracts

class CreateTrail : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTrailBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    var app : MainApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var trail = Trail( generateRandomId(), "","" )
        binding = ActivityCreateTrailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        app!!.markersArray = mutableListOf()
        var edit = false
        if (!edit) {
            app!!.markers = mutableListOf()
        }
        var markers = app!!.markers
        var markerListFragment = MarkerListFragment()
        var bundle = Bundle()
        bundle.putParcelableArrayList("markers", markers as ArrayList<out Parcelable?>?)
        markerListFragment.setArguments(bundle)

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, markerListFragment)
            commit()
        }

        binding.btnNewMarker
            .setOnClickListener{
                trail.name = binding.etTrailName.text.toString()
                trail.description = binding.etTrailDescription.text.toString()
                if (trail.name.isEmpty()) {
                    Snackbar.make(it,R.string.enter_trail_name, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                        app!!.tempTrail = trail.copy()
                        app!!.tempTrailObject.update(app!!.tempTrail)
                        Intent(this, CreateMarker::class.java ).also{
                            refreshIntentLauncher.launch(it)
                }

             }
            }
        fun registerRefreshCallback() {
            refreshIntentLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult())
                {
                    var markers = app!!.markersArray
                    var markerListFragment = MarkerListFragment()
                    var bundle = Bundle()
                    bundle.putParcelableArrayList("markers", markers as ArrayList<out Parcelable?>?)
                    markerListFragment.setArguments(bundle)

                    supportFragmentManager.beginTransaction().apply{
                        replace(R.id.flFragment, markerListFragment)
                        commit()
                    }
                }
        }
        registerRefreshCallback()
        }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.create_trail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miSave -> {
                if (app!!.tempTrail.name.isEmpty()) {
                    Toast.makeText(this,R.string.add_a_marker, Toast.LENGTH_LONG).show()
                } else {
                app!!.trails.create(app!!.tempTrail)
                app!!.resetTempData()
                finish()}

            };
            R.id.miCancel -> {
                app!!.resetTempData()
                finish();
                }
        }
        return true
    }


}
