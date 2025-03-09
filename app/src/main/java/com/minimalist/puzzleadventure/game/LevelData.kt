package com.minimalist.puzzleadventure.game

/**
 * Data class representing a game level's configuration.
 */
data class LevelData(
    val levelNumber: Int,
    val width: Int,
    val height: Int,
    val playerStartX: Int,
    val playerStartY: Int,
    val goalX: Int,
    val goalY: Int,
    val obstacles: List<Obstacle>
)

/**
 * Data class representing an obstacle in the game.
 */
data class Obstacle(
    var x: Int,
    var y: Int,
    val width: Int,
    val height: Int,
    val isMovable: Boolean
) 