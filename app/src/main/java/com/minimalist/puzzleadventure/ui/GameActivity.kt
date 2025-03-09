package com.minimalist.puzzleadventure.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.minimalist.puzzleadventure.MinimalistApp
import com.minimalist.puzzleadventure.R
import com.minimalist.puzzleadventure.databinding.ActivityGameBinding
import com.minimalist.puzzleadventure.game.GameEngine
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Activity for playing a specific puzzle level.
 * Handles game state, user interactions, and level completion.
 */
class GameActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LEVEL_NUMBER = "extra_level_number"
    }

    private lateinit var binding: ActivityGameBinding
    private lateinit var gameEngine: GameEngine
    private val levelManager = MinimalistApp.levelManager
    
    private var levelNumber: Int = 1
    private var moveCount: Int = 0
    private var timeElapsedSeconds: Int = 0
    private var isGamePaused: Boolean = false
    
    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            if (!isGamePaused) {
                timeElapsedSeconds++
                updateTimerDisplay()
            }
            timerHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the level number from the intent
        levelNumber = intent.getIntExtra(EXTRA_LEVEL_NUMBER, 1)
        
        // Initialize the game engine with the level
        gameEngine = GameEngine(binding.gameView, levelNumber)
        gameEngine.setOnMoveListener { 
            moveCount++
            updateMoveCounter()
        }
        gameEngine.setOnLevelCompleteListener {
            onLevelComplete()
        }
        
        setupUI()
        setupClickListeners()
        
        // Start the game
        startGame()
    }

    private fun setupUI() {
        binding.levelTitle.text = getString(R.string.level_select) + " " + levelNumber
        updateMoveCounter()
        updateTimerDisplay()
    }

    private fun setupClickListeners() {
        // Pause button
        binding.btnPause.setOnClickListener {
            pauseGame()
        }
        
        // Restart button
        binding.btnRestart.setOnClickListener {
            restartLevel()
        }
        
        // Pause menu buttons
        binding.btnResume.setOnClickListener {
            resumeGame()
        }
        
        binding.btnRestartPause.setOnClickListener {
            resumeGame()
            restartLevel()
        }
        
        binding.btnMenu.setOnClickListener {
            finish()
        }
        
        // Level complete buttons
        binding.btnNextLevel.setOnClickListener {
            loadNextLevel()
        }
        
        binding.btnMenuComplete.setOnClickListener {
            finish()
        }
    }

    private fun startGame() {
        moveCount = 0
        timeElapsedSeconds = 0
        isGamePaused = false
        
        updateMoveCounter()
        updateTimerDisplay()
        
        // Start the timer
        timerHandler.postDelayed(timerRunnable, 1000)
        
        // Initialize the game view
        gameEngine.startLevel()
    }

    private fun pauseGame() {
        if (!isGamePaused) {
            isGamePaused = true
            binding.pauseOverlay.visibility = View.VISIBLE
            gameEngine.pauseGame()
        }
    }

    private fun resumeGame() {
        if (isGamePaused) {
            isGamePaused = false
            binding.pauseOverlay.visibility = View.GONE
            gameEngine.resumeGame()
        }
    }

    private fun restartLevel() {
        moveCount = 0
        timeElapsedSeconds = 0
        updateMoveCounter()
        updateTimerDisplay()
        gameEngine.restartLevel()
    }

    private fun onLevelComplete() {
        isGamePaused = true
        
        // Save level completion data
        levelManager.markLevelCompleted(levelNumber, moveCount, timeElapsedSeconds)
        
        // Update the completion overlay with stats
        binding.completeMoves.text = getString(R.string.moves, moveCount)
        binding.completeTime.text = getString(R.string.time, formatTime(timeElapsedSeconds))
        
        // Show the completion overlay
        binding.levelCompleteOverlay.visibility = View.VISIBLE
    }

    private fun loadNextLevel() {
        val nextLevel = levelNumber + 1
        if (nextLevel <= levelManager.getLevelCount()) {
            levelNumber = nextLevel
            binding.levelTitle.text = getString(R.string.level_select) + " " + levelNumber
            binding.levelCompleteOverlay.visibility = View.GONE
            
            // Reset game state
            moveCount = 0
            timeElapsedSeconds = 0
            isGamePaused = false
            updateMoveCounter()
            updateTimerDisplay()
            
            // Load the next level
            gameEngine.loadLevel(levelNumber)
        } else {
            // No more levels, return to level select
            finish()
        }
    }

    private fun updateMoveCounter() {
        binding.movesCounter.text = getString(R.string.moves, moveCount)
    }

    private fun updateTimerDisplay() {
        binding.timer.text = getString(R.string.time, formatTime(timeElapsedSeconds))
    }

    private fun formatTime(seconds: Int): String {
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            TimeUnit.SECONDS.toMinutes(seconds.toLong()),
            seconds % 60
        )
    }

    override fun onPause() {
        super.onPause()
        if (!isGamePaused) {
            pauseGame()
        }
        timerHandler.removeCallbacks(timerRunnable)
    }

    override fun onResume() {
        super.onResume()
        if (!binding.levelCompleteOverlay.isShown && !binding.pauseOverlay.isShown) {
            resumeGame()
            timerHandler.postDelayed(timerRunnable, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerHandler.removeCallbacks(timerRunnable)
    }
} 