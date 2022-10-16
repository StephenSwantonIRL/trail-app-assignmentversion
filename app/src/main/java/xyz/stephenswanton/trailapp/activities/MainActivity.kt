package xyz.stephenswanton.trailapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.stephenswanton.trailapp.*
import xyz.stephenswanton.trailapp.databinding.ActivityMainBinding
import xyz.stephenswanton.trailapp.main.MainApp
import xyz.stephenswanton.trailapp.models.Trail

class MainActivity : AppCompatActivity(), TrailListener {
    private lateinit var binding: ActivityMainBinding
    var app : MainApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        binding.rvTrails.layoutManager = LinearLayoutManager(this)
        loadTrails()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miAdd -> Intent(this, CreateTrail::class.java).also{
                startActivity(it)
            }
        }
        return true
    }

    override fun onEditIconClick(trail: Trail) {
        Intent(this, ViewTrail::class.java).also{
            startActivity(it)
        }
    }

    private fun loadTrails() {
        showTrails(app!!.trails.findAll())
    }

    private fun showTrails(trails: List<Trail>) {
        binding.rvTrails.adapter = TrailAdapter(trails, this)
        binding.rvTrails.adapter?.notifyDataSetChanged()
    }

}