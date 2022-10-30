package xyz.stephenswanton.trailapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val IMAGE_REQUEST = 1
    var app : MainApp? = null
    var marker = TrailMarker(generateRandomId(),"0","0", "")
    var latitudeRegex: Regex = """^(\+|-)?(?:90(?:(?:\.0{1,7})?)|(?:[0-9]|[1-8][0-9])(?:(?:\.[0-9]{1,7})?))$""".toRegex()
    var longitudeRegex: Regex = """^(\+|-)?(?:180(?:(?:\.0{1,7})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\.[0-9]{1,7})?))${'$'}""".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMarkerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        var edit = false

        if (intent.hasExtra("marker_edit")) {
            edit = true
            marker = intent.extras?.getParcelable("marker_edit")!!
            binding.etLatitude.setText(marker.latitude)
            binding.etLongitude.setText(marker.longitude)
            binding.etNotes.setText(marker.notes)
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

        binding.btnUseLocation
            .setOnClickListener{

                locationPermissionRequest.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION))

                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location: Location? ->
                                binding.etLatitude.setText(location?.latitude.toString())
                                binding.etLongitude.setText(location?.longitude.toString())

                            }
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

    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            } else -> {
            // No location access granted.
        }
        }
    }
}
