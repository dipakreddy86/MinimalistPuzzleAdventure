package com.minimalist.puzzleadventure.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.minimalist.puzzleadventure.R
import kotlin.math.min

/**
 * Custom view that renders the game board and handles touch interactions.
 */
class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Game state
    private var levelData: LevelData? = null
    private var gameEngine: GameEngine? = null
    
    // Player position
    private var playerX: Int = 0
    private var playerY: Int = 0
    
    // Cell size for rendering
    private var cellSize: Float = 0f
    
    // Paints for drawing
    private val playerPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.player)
        isAntiAlias = true
    }
    
    private val goalPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.goal)
        isAntiAlias = true
    }
    
    private val obstaclePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.obstacle)
        isAntiAlias = true
    }
    
    private val movableObstaclePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.movable)
        isAntiAlias = true
    }
    
    private val gridPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.divider)
        style = Paint.Style.STROKE
        strokeWidth = 1f
        isAntiAlias = true
    }
    
    // Gesture detector for handling swipes
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 == null) return false
            
            // Determine swipe direction
            val dx = e2.x - e1.x
            val dy = e2.y - e1.y
            
            if (Math.abs(dx) > Math.abs(dy)) {
                // Horizontal swipe
                if (dx > 0) {
                    movePlayer(1, 0) // Right
                } else {
                    movePlayer(-1, 0) // Left
                }
            } else {
                // Vertical swipe
                if (dy > 0) {
                    movePlayer(0, 1) // Down
                } else {
                    movePlayer(0, -1) // Up
                }
            }
            
            return true
        }
    })
    
    /**
     * Sets the game engine reference.
     */
    fun setGameEngine(engine: GameEngine) {
        gameEngine = engine
    }
    
    /**
     * Sets the level data and initializes the game state.
     */
    fun setLevelData(data: LevelData) {
        levelData = data
        playerX = data.playerStartX
        playerY = data.playerStartY
        invalidate()
    }
    
    /**
     * Updates the physics simulation.
     */
    fun updatePhysics() {
        // For now, we don't have continuous physics
        // This would be used for animations or dynamic elements
    }
    
    /**
     * Checks if the level is complete (player has reached the goal).
     */
    fun isLevelComplete(): Boolean {
        val data = levelData ?: return false
        return playerX == data.goalX && playerY == data.goalY
    }
    
    /**
     * Moves the player in the specified direction.
     */
    private fun movePlayer(dx: Int, dy: Int) {
        val data = levelData ?: return
        
        // Calculate new position
        var newX = playerX + dx
        var newY = playerY + dy
        
        // Check bounds
        if (newX < 0 || newX >= data.width || newY < 0 || newY >= data.height) {
            return
        }
        
        // Check for obstacles
        val obstacle = findObstacleAt(newX, newY)
        if (obstacle != null) {
            if (obstacle.isMovable) {
                // Try to push the obstacle
                val obstacleNewX = obstacle.x + dx
                val obstacleNewY = obstacle.y + dy
                
                // Check if the obstacle can be moved
                if (obstacleNewX >= 0 && obstacleNewX < data.width &&
                    obstacleNewY >= 0 && obstacleNewY < data.height &&
                    findObstacleAt(obstacleNewX, obstacleNewY) == null) {
                    
                    // Move the obstacle
                    obstacle.x = obstacleNewX
                    obstacle.y = obstacleNewY
                } else {
                    // Can't move the obstacle, so player can't move
                    return
                }
            } else {
                // Can't move through immovable obstacles
                return
            }
        }
        
        // Move the player
        playerX = newX
        playerY = newY
        
        // Notify the game engine that a move was made
        gameEngine?.onMoveMade()
        
        // Redraw the view
        invalidate()
    }
    
    /**
     * Finds an obstacle at the specified position.
     */
    private fun findObstacleAt(x: Int, y: Int): Obstacle? {
        return levelData?.obstacles?.find { it.x == x && it.y == y }
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        
        // Calculate cell size based on view dimensions and level size
        val data = levelData ?: return
        val gridWidth = data.width
        val gridHeight = data.height
        
        cellSize = min(w / gridWidth.toFloat(), h / gridHeight.toFloat())
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val data = levelData ?: return
        
        // Calculate the offset to center the grid
        val offsetX = (width - data.width * cellSize) / 2
        val offsetY = (height - data.height * cellSize) / 2
        
        // Draw the grid
        for (x in 0 until data.width) {
            for (y in 0 until data.height) {
                val left = offsetX + x * cellSize
                val top = offsetY + y * cellSize
                val right = left + cellSize
                val bottom = top + cellSize
                
                canvas.drawRect(left, top, right, bottom, gridPaint)
            }
        }
        
        // Draw the goal
        val goalRect = RectF(
            offsetX + data.goalX * cellSize + cellSize * 0.1f,
            offsetY + data.goalY * cellSize + cellSize * 0.1f,
            offsetX + data.goalX * cellSize + cellSize * 0.9f,
            offsetY + data.goalY * cellSize + cellSize * 0.9f
        )
        canvas.drawOval(goalRect, goalPaint)
        
        // Draw obstacles
        for (obstacle in data.obstacles) {
            val obstacleRect = RectF(
                offsetX + obstacle.x * cellSize + cellSize * 0.05f,
                offsetY + obstacle.y * cellSize + cellSize * 0.05f,
                offsetX + obstacle.x * cellSize + cellSize * 0.95f,
                offsetY + obstacle.y * cellSize + cellSize * 0.95f
            )
            
            canvas.drawRect(
                obstacleRect,
                if (obstacle.isMovable) movableObstaclePaint else obstaclePaint
            )
        }
        
        // Draw the player
        val playerRect = RectF(
            offsetX + playerX * cellSize + cellSize * 0.15f,
            offsetY + playerY * cellSize + cellSize * 0.15f,
            offsetX + playerX * cellSize + cellSize * 0.85f,
            offsetY + playerY * cellSize + cellSize * 0.85f
        )
        canvas.drawRect(playerRect, playerPaint)
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
} 