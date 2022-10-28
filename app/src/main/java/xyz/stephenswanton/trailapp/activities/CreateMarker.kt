package xyz.stephenswanton.trailapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.databinding.ActivityCreateMarkerBinding
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.TrailMarker
import timber.log.Timber
import timber.log.Timber.i
import xyz.stephenswanton.trailapp.models.generateRandomId

class CreateMarker : AppCompatActivity() {
    private lateinit var binding: ActivityCreateMarkerBinding

    var app : MainApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMarkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        var marker = TrailMarker(generateRandomId(),"0","0", "")
        var edit = false

        if (intent.hasExtra("marker_edit")) {
            edit = true
            marker = intent.extras?.getParcelable("marker_edit")!!
            binding.etLatitude.setText(marker.latitude)
            binding.etLongitude.setText(marker.longitude)
            binding.btnSaveMarker.setText(R.string.save_marker)
        }


        binding.btnSaveMarker
            .setOnClickListener{
                marker.latitude = binding.etLatitude.text.toString()
                marker.longitude = binding.etLongitude.text.toString()
                marker.notes = binding.etNotes.text.toString()
                if (marker.latitude.isEmpty() || marker.longitude.isEmpty() ) {
                    if (marker.latitude.isEmpty()) {
                        Snackbar.make(it, R.string.enter_latitude, Snackbar.LENGTH_LONG)
                            .show()
                    }
                    if (marker.longitude.isEmpty()) {
                        Snackbar.make(it, R.string.enter_longitude, Snackbar.LENGTH_LONG)
                            .show()
                    }
                } else {
                    if(edit){
                        app!!.markersArray = app!!.markersArray.filter{item -> item.id != marker.id} as MutableList<TrailMarker>
                    }
                    app!!.markersArray.add(marker.copy())
                    app!!.tempTrail.markers = mutableListOf<TrailMarker>()
                    app!!.tempTrail.markers.addAll(app!!.markersArray)
                    app!!.tempTrailObject.update(app!!.tempTrail)
                    setResult(RESULT_OK)
                    if(edit){
                        Intent(this, MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            putExtra("trail_view", app!!.trails.findById(app!!.trails.idContainingMarker(marker.id)?: 0))
                        }.also {
                            startActivity(it)
                        }
                    }
                    finish()
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
