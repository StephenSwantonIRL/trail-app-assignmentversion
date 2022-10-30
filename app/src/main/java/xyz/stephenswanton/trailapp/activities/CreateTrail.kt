package xyz.stephenswanton.trailapp.activities

import android.R.array
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber.i
import xyz.stephenswanton.trailapp.R
import xyz.stephenswanton.trailapp.databinding.ActivityCreateTrailBinding
import xyz.stephenswanton.trailapp.fragments.MarkerListFragment
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.Trail
import xyz.stephenswanton.trailapp.models.generateRandomId
import java.util.*
import kotlin.collections.ArrayList


class CreateTrail : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTrailBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    var app : MainApp? = null
    var edit: Boolean = false
    var trail = Trail( generateRandomId(), "","" )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTrailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp
        app!!.markersArray = mutableListOf()
        edit = false
        if (intent.hasExtra("trail_edit")) {

            edit = true
            trail = intent.extras?.getParcelable<Trail>("trail_edit")!!
            app!!.tempTrail = app!!.trails.findById(trail.id) ?: trail
            binding.etTrailName.setText(trail.name)
            binding.etTrailDescription.setText(trail.description)
            var trailTypes = resources.getStringArray(R.array.trail_type)
            i(trailTypes.toString())
            var spinnerPosition = trailTypes.indexOf(trail.trailType) as Int
            i("SpinnerPosition")
            i(spinnerPosition.toString())
            binding.spTrailType.setSelection(spinnerPosition)
        }

        if (!edit) {
            app!!.markers = mutableListOf()
        } else {
            app!!.markers = app!!.tempTrail.markers
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

        binding.spTrailType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                trail.trailType = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
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
                    app!!.tempTrail.name = binding.etTrailName.text.toString()
                    app!!.tempTrail.trailType = trail.trailType.toString()
                    app!!.tempTrail.description = binding.etTrailDescription.text.toString()
                    if(edit){
                        app!!.trails.update(app!!.tempTrail.copy())
                    } else {
                        app!!.trails.create(app!!.tempTrail.copy())
                    }
                app!!.resetTempData()
                Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }.also {
                    startActivity(it)
                }
                }

            };
            R.id.miCancel -> {
                app!!.resetTempData()
                finish();
                }
        }
        return true
    }


}
