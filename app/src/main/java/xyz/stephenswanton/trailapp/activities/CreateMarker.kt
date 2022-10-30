package xyz.stephenswanton.trailapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.databinding.ActivityCreateMarkerBinding
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.TrailMarker
import timber.log.Timber
import timber.log.Timber.i
import xyz.stephenswanton.trailapp.helpers.showImagePicker
import xyz.stephenswanton.trailapp.models.generateRandomId

class CreateMarker : AppCompatActivity() {
    private lateinit var binding: ActivityCreateMarkerBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    val IMAGE_REQUEST = 1
    var app : MainApp? = null
    var marker = TrailMarker(generateRandomId(),"0","0", "")
    var latitudeRegex: Regex = """^(\+|-)?(?:90(?:(?:\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\.[0-9]{1,6})?))$""".toRegex()
    var longitudeRegex: Regex = """^(\+|-)?(?:180(?:(?:\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\.[0-9]{1,6})?))${'$'}""".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMarkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        var edit = false

        if (intent.hasExtra("marker_edit")) {
            edit = true
            marker = intent.extras?.getParcelable("marker_edit")!!
            binding.etLatitude.setText(marker.latitude)
            binding.etLongitude.setText(marker.longitude)
            binding.btnSaveMarker.setText(R.string.save_marker)
            Picasso.get()
                .load(marker.image)
                .into(binding.ivMarkerImage)
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
                } else if (!latitudeRegex.matches(marker.latitude) || !longitudeRegex.matches(marker.longitude) ) {
                    if (!latitudeRegex.matches(marker.latitude)) {
                        Snackbar.make(it, R.string.enter_valid_latitude, Snackbar.LENGTH_LONG)
                            .show()
                    }
                    if (!longitudeRegex.matches(marker.longitude)) {
                        Snackbar.make(it, R.string.enter_valid_longitude, Snackbar.LENGTH_LONG)
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

        binding.btnAddImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()


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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            marker.image = result.data!!.data!!
                            Picasso.get()
                                .load(marker.image)
                                .into(binding.ivMarkerImage)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
