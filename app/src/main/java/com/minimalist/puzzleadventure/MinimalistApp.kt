package com.minimalist.puzzleadventure

import android.app.Application
import com.minimalist.puzzleadventure.game.LevelManager

/**
 * Application class for initializing app-wide components.
 */
class MinimalistApp : Application() {

    companion object {
        lateinit var levelManager: LevelManager
            private set
    }

    override fun onCreate() {
        super.onCreate()
        
        // Initialize the level manager
        levelManager = LevelManager()
        levelManager.initialize(this)
    }
} 