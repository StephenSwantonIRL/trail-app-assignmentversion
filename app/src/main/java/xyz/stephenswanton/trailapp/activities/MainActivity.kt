package xyz.stephenswanton.trailapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.stephenswanton.trailapp.*
import xyz.stephenswanton.trailapp.databinding.ActivityMainBinding
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.Trail
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import timber.log.Timber.i


class MainActivity : AppCompatActivity(), TrailListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    var app : MainApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        binding.rvTrails.layoutManager = LinearLayoutManager(this)
        var trail: Trail?
        registerRefreshCallback()
        if (intent.hasExtra("trail_view")) {
            trail = intent.extras?.getParcelable("trail_view")
            if(trail != null) {
                trail.markers = app!!.tempTrail.markers
                Intent(this, ViewTrail::class.java).apply {
                    putExtra("trail_view", trail)
                }.also {
                    refreshIntentLauncher.launch(it)
                }
            }
        }
        if (intent.hasExtra("marker_view")) {
            i("sent extra marker_view")
            intent.extras?.getLong("marker_view").also{
                trail = it?.let { item -> app!!.trails.findById(app!!.trails.idContainingMarker(item)?: 0)}
            }
            if(trail != null) {
                Intent(this, ViewTrail::class.java).apply {
                    putExtra("trail_view", trail)
                }.also {
                    refreshIntentLauncher.launch(it)
                }
            }
        }



        loadTrails()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miAdd -> Intent(this, CreateTrail::class.java).also{
                refreshIntentLauncher.launch(it)
            }
            R.id.miDeleteAll -> {

                val deleteAllDialog = AlertDialog.Builder(this)
                    .setTitle("Delete All Trails")
                    .setMessage("Are you sure you want to delete all Trails?")
                    .setPositiveButton("Yes"){ _, _ ->
                        Toast.makeText(this, "You selected Yes, Trails Deleted", Toast.LENGTH_LONG)
                        app!!.trails.deleteAll()
                        Intent(this, MainActivity::class.java).apply{
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        }.also {
                            refreshIntentLauncher.launch(it)
                        }
                    }
                    .setNegativeButton("No"){ _, _ ->
                        Toast.makeText(this, "You selected No", Toast.LENGTH_LONG)
                    }
                deleteAllDialog.show()


            }
        }
        return true
    }

    override fun onEditIconClick(trail: Trail) {
        Intent(this, ViewTrail::class.java).apply{
            putExtra("trail_view", trail)

        }.also{
            refreshIntentLauncher.launch(it)
        }
    }

    override fun onDeleteTrailIconClick(trail: Trail) {
        app!!.trails.deleteById(trail.id)
        loadTrails()
    }

    private fun loadTrails() {
        showTrails(app!!.trails.findAll())
    }

    private fun showTrails(trails: List<Trail>) {
        binding.rvTrails.adapter = TrailAdapter(trails, this)
        binding.rvTrails.adapter?.notifyDataSetChanged()
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { binding.rvTrails.adapter?.notifyDataSetChanged() }
    }

}