package xyz.stephenswanton.trailapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.TrailMarker
import timber.log.Timber
import timber.log.Timber.i
import xyz.stephenswanton.trailapp.databinding.ActivityViewMarkerBinding
import xyz.stephenswanton.trailapp.models.generateRandomId

class ViewMarker : AppCompatActivity() {
    private lateinit var binding: ActivityViewMarkerBinding

    var app : MainApp? = null
    var marker = TrailMarker(generateRandomId(),"0","0", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMarkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        var edit = false

        if (intent.hasExtra("marker_view")) {
            edit = true
            marker = intent.extras?.getParcelable("marker_view")!!
            binding.tvLatitudeView.setText(marker.latitude)
            binding.tvLongitudeView.setText(marker.longitude)
            if(marker.notes !="") {
                binding.tvNotesView.setText(marker.notes)
            } else {
                binding.tvNotesView.setText("No notes provided")
            }
            Picasso.get()
                .load(marker.image)
                .into(binding.ivMarkerImage)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.create_marker_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miCancel -> finish();
        }
        return true
    }


}
