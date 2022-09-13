package xyz.stephenswanton.trailapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.stephenswanton.trailapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}