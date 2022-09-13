package xyz.stephenswanton.trailapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.stephenswanton.trailapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trails = mutableListOf(
            Trail("Trail 1")
        )
        val adapter = TrailAdapter(trails)
        binding.rvTrails.adapter = adapter
        binding.rvTrails.layoutManager = LinearLayoutManager(this)
    }
}