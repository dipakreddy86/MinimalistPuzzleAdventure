package com.minimalist.puzzleadventure.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.minimalist.puzzleadventure.MinimalistApp
import com.minimalist.puzzleadventure.R
import com.minimalist.puzzleadventure.databinding.ActivityLevelSelectBinding

/**
 * Activity for selecting a level to play.
 * Displays a grid of available levels with their completion status.
 */
class LevelSelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelSelectBinding
    private lateinit var adapter: LevelAdapter
    private val levelManager = MinimalistApp.levelManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        adapter = LevelAdapter(levelManager.getLevelCount()) { levelNumber ->
            if (levelManager.isLevelUnlocked(levelNumber)) {
                val intent = Intent(this, GameActivity::class.java).apply {
                    putExtra(GameActivity.EXTRA_LEVEL_NUMBER, levelNumber)
                }
                startActivity(intent)
            }
        }

        binding.levelGrid.apply {
            layoutManager = GridLayoutManager(this@LevelSelectActivity, 3)
            adapter = this@LevelSelectActivity.adapter
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the level status when returning to this screen
        adapter.notifyDataSetChanged()
    }

    /**
     * Adapter for the level selection grid.
     */
    inner class LevelAdapter(
        private val levelCount: Int,
        private val onLevelSelected: (Int) -> Unit
    ) : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_level, parent, false)
            return LevelViewHolder(view)
        }

        override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
            val levelNumber = position + 1
            holder.bind(levelNumber)
        }

        override fun getItemCount(): Int = levelCount

        inner class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val levelNumberText: TextView = itemView.findViewById(R.id.level_number)
            private val levelStatusIcon: ImageView = itemView.findViewById(R.id.level_status)
            private val levelLockIcon: ImageView = itemView.findViewById(R.id.level_lock)

            fun bind(levelNumber: Int) {
                levelNumberText.text = levelNumber.toString()

                val isUnlocked = levelManager.isLevelUnlocked(levelNumber)
                val isCompleted = levelManager.isLevelCompleted(levelNumber)

                // Show/hide lock icon
                levelLockIcon.visibility = if (isUnlocked) View.GONE else View.VISIBLE
                
                // Show/hide completion status
                levelStatusIcon.visibility = if (isCompleted) View.VISIBLE else View.GONE
                
                // Set the level number visibility based on lock status
                levelNumberText.visibility = if (isUnlocked) View.VISIBLE else View.GONE

                // Set click listener
                itemView.setOnClickListener {
                    onLevelSelected(levelNumber)
                }
            }
        }
    }
} 