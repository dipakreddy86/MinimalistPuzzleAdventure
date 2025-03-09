package com.minimalist.puzzleadventure.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minimalist.puzzleadventure.R
import com.minimalist.puzzleadventure.databinding.ActivityMainBinding

/**
 * Main menu activity that serves as the entry point to the game.
 * Provides options to play, access settings, or view about information.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Play button - navigate to level selection
        binding.btnPlay.setOnClickListener {
            startActivity(Intent(this, LevelSelectActivity::class.java))
        }

        // Settings button - to be implemented
        binding.btnSettings.setOnClickListener {
            // TODO: Implement settings screen
            // For now, we'll just show a toast message
            android.widget.Toast.makeText(
                this,
                "Settings coming soon!",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }

        // About button - to be implemented
        binding.btnAbout.setOnClickListener {
            // TODO: Implement about screen
            // For now, we'll just show a toast message
            android.widget.Toast.makeText(
                this,
                getString(R.string.about_text),
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    }
} 