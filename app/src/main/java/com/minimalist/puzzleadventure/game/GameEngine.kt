package com.minimalist.puzzleadventure.game

import android.os.Handler
import android.os.Looper

/**
 * Core game engine that manages the game state, physics, and interactions.
 * This class connects the game view with the level data and handles game logic.
 */
class GameEngine(
    private val gameView: GameView,
    private var currentLevel: Int
) {
    
    // Callbacks for game events
    private var onMoveListener: (() -> Unit)? = null
    private var onLevelCompleteListener: (() -> Unit)? = null
    
    // Game state
    private var isGameActive = false
    private var isPaused = false
    
    // Handler for game loop
    private val gameHandler = Handler(Looper.getMainLooper())
    private val gameLoopRunnable = object : Runnable {
        override fun run() {
            if (isGameActive && !isPaused) {
                update()
                gameView.invalidate() // Trigger redraw
                gameHandler.postDelayed(this, 16) // ~60 FPS
            }
        }
    }
    
    init {
        // Set up the game view
        gameView.setGameEngine(this)
        
        // Load the initial level
        loadLevel(currentLevel)
    }
    
    /**
     * Starts the current level.
     */
    fun startLevel() {
        isGameActive = true
        isPaused = false
        startGameLoop()
    }
    
    /**
     * Pauses the game.
     */
    fun pauseGame() {
        isPaused = true
        stopGameLoop()
    }
    
    /**
     * Resumes the game after pausing.
     */
    fun resumeGame() {
        isPaused = false
        startGameLoop()
    }
    
    /**
     * Restarts the current level.
     */
    fun restartLevel() {
        loadLevel(currentLevel)
        startLevel()
    }
    
    /**
     * Loads a specific level.
     */
    fun loadLevel(levelNumber: Int) {
        currentLevel = levelNumber
        
        // TODO: Load level data from a level repository or file
        // For now, we'll create a simple test level
        val levelData = createTestLevel(levelNumber)
        
        // Set up the game view with the level data
        gameView.setLevelData(levelData)
    }
    
    /**
     * Sets a listener for when the player makes a move.
     */
    fun setOnMoveListener(listener: () -> Unit) {
        onMoveListener = listener
    }
    
    /**
     * Sets a listener for when the level is completed.
     */
    fun setOnLevelCompleteListener(listener: () -> Unit) {
        onLevelCompleteListener = listener
    }
    
    /**
     * Notifies that a move has been made.
     */
    fun onMoveMade() {
        onMoveListener?.invoke()
        checkLevelCompletion()
    }
    
    /**
     * Checks if the level has been completed.
     */
    private fun checkLevelCompletion() {
        // TODO: Implement actual level completion logic
        // For now, we'll simulate level completion after a few moves
        if (gameView.isLevelComplete()) {
            isGameActive = false
            stopGameLoop()
            
            // Notify the listener with a slight delay for visual feedback
            Handler(Looper.getMainLooper()).postDelayed({
                onLevelCompleteListener?.invoke()
            }, 500)
        }
    }
    
    /**
     * Updates the game state.
     */
    private fun update() {
        // Update physics, check collisions, etc.
        gameView.updatePhysics()
    }
    
    /**
     * Starts the game loop.
     */
    private fun startGameLoop() {
        gameHandler.post(gameLoopRunnable)
    }
    
    /**
     * Stops the game loop.
     */
    private fun stopGameLoop() {
        gameHandler.removeCallbacks(gameLoopRunnable)
    }
    
    /**
     * Creates a test level for development purposes.
     */
    private fun createTestLevel(levelNumber: Int): LevelData {
        // Create a simple level with increasing difficulty based on level number
        return LevelData(
            levelNumber = levelNumber,
            width = 10,
            height = 10,
            playerStartX = 1,
            playerStartY = 1,
            goalX = 8,
            goalY = 8,
            obstacles = generateObstacles(levelNumber)
        )
    }
    
    /**
     * Generates obstacles for a test level.
     */
    private fun generateObstacles(levelNumber: Int): List<Obstacle> {
        val obstacles = mutableListOf<Obstacle>()
        
        // Add more obstacles as the level number increases
        val obstacleCount = 3 + levelNumber
        
        for (i in 0 until obstacleCount) {
            // Create some simple obstacles
            // Avoid placing obstacles at player start or goal positions
            val x = (2 + i % 7)
            val y = (2 + i / 7 * 2)
            
            obstacles.add(
                Obstacle(
                    x = x,
                    y = y,
                    width = 1,
                    height = 1,
                    isMovable = i % 3 == 0
                )
            )
        }
        
        return obstacles
    }
} 