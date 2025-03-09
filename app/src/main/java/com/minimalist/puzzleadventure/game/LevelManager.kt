package com.minimalist.puzzleadventure.game

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages level data, including unlocking, completion status, and statistics.
 */
class LevelManager {
    
    companion object {
        private const val PREFS_NAME = "puzzle_adventure_prefs"
        private const val KEY_LEVEL_COMPLETED_PREFIX = "level_completed_"
        private const val KEY_LEVEL_MOVES_PREFIX = "level_moves_"
        private const val KEY_LEVEL_TIME_PREFIX = "level_time_"
        
        // Total number of levels in the game
        private const val TOTAL_LEVELS = 20
    }
    
    private var sharedPreferences: SharedPreferences? = null
    
    /**
     * Initialize with a context to access SharedPreferences.
     * This should be called from an Activity or Application context.
     */
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * Returns the total number of levels in the game.
     */
    fun getLevelCount(): Int {
        return TOTAL_LEVELS
    }
    
    /**
     * Checks if a level is unlocked and available to play.
     * Level 1 is always unlocked, other levels require the previous level to be completed.
     */
    fun isLevelUnlocked(levelNumber: Int): Boolean {
        if (levelNumber == 1) return true
        
        // For levels beyond 1, the previous level must be completed
        return isLevelCompleted(levelNumber - 1)
    }
    
    /**
     * Checks if a level has been completed by the player.
     */
    fun isLevelCompleted(levelNumber: Int): Boolean {
        return sharedPreferences?.getBoolean(KEY_LEVEL_COMPLETED_PREFIX + levelNumber, false) ?: false
    }
    
    /**
     * Marks a level as completed and saves the player's performance data.
     */
    fun markLevelCompleted(levelNumber: Int, moves: Int, timeSeconds: Int) {
        sharedPreferences?.edit()?.apply {
            putBoolean(KEY_LEVEL_COMPLETED_PREFIX + levelNumber, true)
            
            // Only update moves and time if they're better than previous records
            // or if this is the first completion
            val currentBestMoves = getLevelMoves(levelNumber)
            if (currentBestMoves == 0 || moves < currentBestMoves) {
                putInt(KEY_LEVEL_MOVES_PREFIX + levelNumber, moves)
            }
            
            val currentBestTime = getLevelTime(levelNumber)
            if (currentBestTime == 0 || timeSeconds < currentBestTime) {
                putInt(KEY_LEVEL_TIME_PREFIX + levelNumber, timeSeconds)
            }
            
            apply()
        }
    }
    
    /**
     * Gets the best (lowest) number of moves used to complete a level.
     * Returns 0 if the level hasn't been completed.
     */
    fun getLevelMoves(levelNumber: Int): Int {
        return sharedPreferences?.getInt(KEY_LEVEL_MOVES_PREFIX + levelNumber, 0) ?: 0
    }
    
    /**
     * Gets the best (fastest) time used to complete a level, in seconds.
     * Returns 0 if the level hasn't been completed.
     */
    fun getLevelTime(levelNumber: Int): Int {
        return sharedPreferences?.getInt(KEY_LEVEL_TIME_PREFIX + levelNumber, 0) ?: 0
    }
    
    /**
     * Resets all level progress data.
     */
    fun resetAllProgress() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
} 